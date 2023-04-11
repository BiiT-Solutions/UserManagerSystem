package com.biit.usermanager.security.activities;

import com.biit.usermanager.logger.UserManagerLogger;
import com.biit.utils.configuration.ConfigurationReader;
import com.biit.utils.configuration.PropertiesSourceFile;
import com.biit.utils.configuration.SourceFile;
import com.biit.utils.configuration.SystemVariablePropertiesSourceFile;
import com.biit.utils.configuration.exceptions.PropertyNotFoundException;
import com.biit.utils.file.watcher.FileWatcher.FileModifiedListener;

import java.io.File;
import java.nio.file.Path;
import java.util.Set;

public class RoleConfigurationReader extends ConfigurationReader {
    private static final String CONFIG_FILE = "roleActivities.conf";
    private static final String SYSTEM_VARIABLE_CONFIG = "ROLES_CONFIG";

    private static final String PERMISSIONS_SUFFIX = "permissions";
    private static final String TRANSLATION_SUFFIX = "translation";

    private static final class InstanceHolder {
        private static final RoleConfigurationReader instance = new RoleConfigurationReader();
    }

    //@FindBugsSuppressWarnings(value = "DC_DOUBLECHECK")
    public static RoleConfigurationReader getInstance() {
        return InstanceHolder.instance;
    }

    private RoleConfigurationReader() {
        super();

        /*
         * We do not want to merge old roles with new ones defined in system variable.
         * Use only one file or the other.
         */
        final File systemFile = new File(SourceFile.readEnvironmentVariable(SYSTEM_VARIABLE_CONFIG) + File.separator + CONFIG_FILE);
        if (SourceFile.readEnvironmentVariable(SYSTEM_VARIABLE_CONFIG) == null || !systemFile.exists()) {
            final PropertiesSourceFile sourceFile = new PropertiesSourceFile(CONFIG_FILE);
            sourceFile.addFileModifiedListeners(new FileModifiedListener() {

                @Override
                public void changeDetected(Path pathToFile) {
                    UserManagerLogger.info(this.getClass().getName(), "WAR configuration file '" + pathToFile + "' change detected.");
                    readConfigurations();
                }
            });
            addPropertiesSource(sourceFile);
        } else {
            final SystemVariablePropertiesSourceFile systemSourceFile = new SystemVariablePropertiesSourceFile(SYSTEM_VARIABLE_CONFIG, CONFIG_FILE);
            systemSourceFile.addFileModifiedListeners(new FileModifiedListener() {

                @Override
                public void changeDetected(Path pathToFile) {
                    UserManagerLogger.info(this.getClass().getName(), "System variable configuration file '" + pathToFile + "' change detected.");
                    readConfigurations();
                }
            });
            addPropertiesSource(systemSourceFile);
        }

        // Initialize all properties with default value. Not initialized
        // properties are ignored.
        initializeAllProperties();
        readConfigurations();
    }

    private String getPropertyException(String propertyId) {
        try {
            return getProperty(propertyId);
        } catch (PropertyNotFoundException e) {
            return null;
        }
    }

    private String[] getPropertyCommaSeparatedValuesLogException(String propertyId) {
        try {
            return getCommaSeparatedValues(propertyId);
        } catch (PropertyNotFoundException e) {
            UserManagerLogger.warning(this.getClass().getName(), "Values for '" + propertyId + "' not found in role assignations.");
            return null;
        }
    }

    public Set<String> getAllRolesDefinitions() {
        return getAllPropertiesPrefixes();
    }

    public String[] getRoleActivities(String roleName) {
        return getPropertyCommaSeparatedValuesLogException(roleName + "." + PERMISSIONS_SUFFIX);
    }

    public String getRoleTranslationCode(String roleName) {
        return getPropertyException(roleName + "." + TRANSLATION_SUFFIX);
    }
}
