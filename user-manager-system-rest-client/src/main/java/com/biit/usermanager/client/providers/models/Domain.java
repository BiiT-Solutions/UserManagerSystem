package com.biit.usermanager.client.providers.models;

/*-
 * #%L
 * User Manager System (Rest Client)
 * %%
 * Copyright (C) 2022 - 2025 BiiT Sourcing Solutions S.L.
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * #L%
 */


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
