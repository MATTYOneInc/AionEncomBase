package com.aionemu.commons.scripting.scriptmanager;

import java.util.Set;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(
   name = "scriptlist"
)
@XmlAccessorType(XmlAccessType.NONE)
public class ScriptList {
   @XmlElement(
      name = "scriptinfo",
      type = ScriptInfo.class
   )
   private Set<ScriptInfo> scriptInfos;

   public Set<ScriptInfo> getScriptInfos() {
      return this.scriptInfos;
   }

   public void setScriptInfos(Set<ScriptInfo> scriptInfos) {
      this.scriptInfos = scriptInfos;
   }

   public String toString() {
      StringBuilder sb = new StringBuilder();
      sb.append("ScriptList");
      sb.append("{scriptInfos=").append(this.scriptInfos);
      sb.append('}');
      return sb.toString();
   }
}
