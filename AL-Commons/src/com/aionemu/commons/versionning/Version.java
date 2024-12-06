package com.aionemu.commons.versionning;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.jar.Attributes;
import java.util.jar.JarFile;
import java.util.jar.Manifest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Version {
   private static final Logger log = LoggerFactory.getLogger(Version.class);
   private String revision;
   private String date;
   private String branch;
   private String commitTime;

   public Version() {
   }

   public Version(Class<?> c) {
      this.loadInformation(c);
   }

   public void loadInformation(Class<?> c) {
      File jarName = null;

      try {
         jarName = Locator.getClassSource(c);
         JarFile jarFile = new JarFile(jarName);
         Attributes attrs = jarFile.getManifest().getMainAttributes();
         this.revision = this.getAttribute("Revision", attrs);
         this.date = this.getAttribute("Date", attrs);
         this.branch = this.getAttribute("Branch", attrs);
         this.commitTime = this.getAttribute("CommitTime", attrs);
      } catch (IOException var5) {
         log.error("Unable to get Soft information\nFile name '" + (jarName == null ? "null" : jarName.getAbsolutePath()) + "' isn't a valid jar", var5);
      }

   }

   public void transferInfo(String jarName, String type, File fileToWrite) {
      try {
         if (!fileToWrite.exists()) {
            log.error("Unable to Find File :" + fileToWrite.getName() + " Please Update your " + type);
            return;
         }

         JarFile jarFile = new JarFile("./" + jarName);
         Manifest manifest = jarFile.getManifest();
         OutputStream fos = new FileOutputStream(fileToWrite);
         manifest.write(fos);
         fos.close();
      } catch (IOException var7) {
         log.error("Error, " + var7);
      }

   }

   public final String getRevision() {
      return this.revision;
   }

   public final String getDate() {
      return this.date;
   }

   public final String getBranch() {
      return this.branch;
   }

   public final String getCommitTime() {
      return this.commitTime;
   }

   private final String getAttribute(String attribute, Attributes attrs) {
      String date = attrs.getValue(attribute);
      return date != null ? date : "Unknown " + attribute;
   }
}
