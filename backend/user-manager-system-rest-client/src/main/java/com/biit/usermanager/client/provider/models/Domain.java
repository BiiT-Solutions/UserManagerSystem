package com.biit.usermanager.client.provider.models;


import com.biit.logger.ExceptionType;
import com.biit.server.exceptions.UnexpectedValueException;

public class Domain {

    private String domain;

    public Domain(String domain) {
        this.setDomain(domain);
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        if (!isValidDomain(domain)) {
            throw new UnexpectedValueException(getClass(), "Domain provided is not a proper domain.", ExceptionType.WARNING);
        }
        this.domain = domain;
    }

    private boolean isValidDomain(String addStr) {
        boolean ret = true;

        if ("".equals(addStr) || addStr == null) {
            ret = false;
        } else if (addStr.startsWith("-") || addStr.endsWith("-")) {
            ret = false;
        } else if (!addStr.contains(".")) {
            ret = false;
        } else {
            // Split domain into String array.
            final String[] domainEle = addStr.split("\\.");
            int size = domainEle.length;
            // Loop in the domain String array.
            for (int i = 0; i < size; i++) {
                // If one domain part string is empty, then return false.
                final String domainEleStr = domainEle[i];
                if ("".equals(domainEleStr.trim())) {
                    return false;
                }
            }

            // Get domain char array.
            final char[] domainChar = addStr.toCharArray();
            size = domainChar.length;
            // Loop in the char array.
            for (int i = 0; i < size; i++) {
                // Get each char in the array.
                final char eleChar = domainChar[i];
                final String charStr = String.valueOf(eleChar);

                // If char value is not a valid domain character then return false.
                if (!".".equals(charStr) && !"-".equals(charStr) && !charStr.matches("[a-zA-Z]") && !charStr.matches("[0-9]")) {
                    ret = false;
                    break;
                }
            }
        }
        return ret;
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof Domain) {
            return ((Domain) o).domain.equalsIgnoreCase(this.domain);
        }
        return super.equals(o);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }
}
