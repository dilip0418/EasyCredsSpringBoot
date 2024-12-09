package com.dilip.ccms.personalDetails;

import java.time.LocalDate;

/**
 * Projection for {@link PersonalDetails}
 */
public interface PersonalDetailsInfo {
    Integer getId();

    String getFirstName();

    String getLastName();

    LocalDate getDateOfBirth();

    AddressInfo getAddress();

    EmploymentStatusInfo getEmploymentStatus();

    /**
     * Projection for {@link com.dilip.ccms.address.Address}
     */
    interface AddressInfo {
        String getStreet();

        CityInfo getCity();

        StateInfo getState();

        /**
         * Projection for {@link com.dilip.ccms.address.City}
         */
        interface CityInfo {
            String getCityName();
        }

        /**
         * Projection for {@link com.dilip.ccms.address.State}
         */
        interface StateInfo {
            String getName();

            String getStateCode();
        }
    }

    interface EmploymentStatusInfo {
        String getStatusName();
    }


}