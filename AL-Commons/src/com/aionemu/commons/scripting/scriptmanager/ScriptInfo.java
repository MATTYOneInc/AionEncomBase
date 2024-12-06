package com.aionemu.commons.scripting.scriptmanager;

import java.io.File;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(
   name = "scriptinfo"
)
@XmlAccessorType(XmlAccessType.NONE)
public class ScriptInfo {
   @XmlAttribute(
      required = true
   )
   private File root;
   @XmlElement(
      name = "library"
   )
   private List<File> libraries;
   @XmlElement(
      name = "scriptinfo"
   )
   private List<ScriptInfo> scriptInfos;
   @XmlElement(
      name = "compiler"
   )
   private String compilerClass;

   public ScriptInfo() {
      this.compilerClass = ScriptManager.DEFAULT_COMPILER_CLASS.getName();
   }

   public File getRoot() {
      return this.root;
   }

   public void setRoot(File root) {
      this.root = root;
   }

   public List<File> getLibraries() {
      return this.libraries;
   }

   public void setLibraries(List<File> libraries) {
      this.libraries = libraries;
   }

   public List<ScriptInfo> getScriptInfos() {
      return this.scriptInfos;
   }

   public void setScriptInfos(List<ScriptInfo> scriptInfos) {
      this.scriptInfos = scriptInfos;
   }

   public String getCompilerClass() {
      return this.compilerClass;
   }

   public void setCompilerClass(String compilerClass) {
      this.compilerClass = compilerClass;
   }

   public boolean equals(Object o) {
      if (this == o) {
         return true;
      } else if (o != null && this.getClass() == o.getClass()) {
         ScriptInfo that = (ScriptInfo)o;
         return this.root.equals(that.root);
      } else {
         return false;
      }
   }

   public int hashCode() {
      return this.root.hashCode();
   }

   public String toString() {
      StringBuilder sb = new StringBuilder();
      sb.append("ScriptInfo");
      sb.append("{root=").append(this.root);
      sb.append(", libraries=").append(this.libraries);
      sb.append(", compilerClass='").append(this.compilerClass).append('\'');
      sb.append(", scriptInfos=").append(this.scriptInfos);
      sb.append('}');
      return sb.toString();
   }
}
