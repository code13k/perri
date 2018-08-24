package org.code13k.perri.lib;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Enumeration;
import java.util.jar.Attributes;
import java.util.jar.JarFile;
import java.util.jar.Manifest;

public class Util {
    /**
     * Get app version from manifest info
     */
    public static String getApplicationVersion() {
        Enumeration resourceEnum;
        try {
            resourceEnum = Thread.currentThread().getContextClassLoader().getResources(JarFile.MANIFEST_NAME);
            while (resourceEnum.hasMoreElements()) {
                try {
                    URL url = (URL) resourceEnum.nextElement();
                    InputStream is = url.openStream();
                    if (is != null) {
                        Manifest manifest = new Manifest(is);
                        Attributes attr = manifest.getMainAttributes();
                        String version = attr.getValue("Implementation-Version");
                        if (version != null) {
                            return version;
                        }
                    }
                } catch (Exception e) {
                    // Nothing
                }
            }
        } catch (IOException e1) {
            // Nothing
        }
        return null;
    }
}
