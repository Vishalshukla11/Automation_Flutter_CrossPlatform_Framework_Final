package com.briskpe.smeportal.enums;

public enum Platform {
    WEB, ANDROID, IOS, MOBILE_WEB;

    /**
     * Converts a string to the corresponding Platform enum, case-insensitively.
     * Throws an IllegalArgumentException with detailed message if the input is
     * invalid.
     *
     * @param platform the input string representing platform name
     * @return the matching Platform enum value
     */
    public static Platform fromString(String platform) {
        if (platform == null || platform.trim().isEmpty()) {
            throw new IllegalArgumentException(
                    "Platform is null or empty. Please set it using -Dplatform or in global.properties. " +
                            "Supported platforms: WEB, ANDROID, IOS, MOBILE_WEB.");
        }
        try {
            return Platform.valueOf(platform.trim().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException(
                    "Invalid platform: '" + platform + "'. Supported platforms are: WEB, ANDROID, IOS, MOBILE_WEB.", e);
        }
    }
}
