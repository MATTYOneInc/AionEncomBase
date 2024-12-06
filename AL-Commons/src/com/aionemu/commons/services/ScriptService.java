package com.aionemu.commons.services;

import com.aionemu.commons.scripting.scriptmanager.ScriptManager;
import java.io.File;
import java.util.Collections;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import javolution.util.FastMap;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ScriptService {
   private static final Logger log = LoggerFactory.getLogger(ScriptService.class);
   private final Map<File, ScriptManager> map = (new FastMap()).shared();

   public void load(String file) throws RuntimeException {
      this.load(new File(file));
   }

   public void load(File file) throws RuntimeException {
      if (file.isFile()) {
         this.loadFile(file);
      } else if (file.isDirectory()) {
         this.loadDir(file);
      }

   }

   private void loadFile(File file) {
      if (this.map.containsKey(file)) {
         throw new IllegalArgumentException("ScriptManager by file:" + file + " already loaded");
      } else {
         ScriptManager sm = new ScriptManager();

         try {
            sm.load(file);
         } catch (Exception var4) {
            log.error("loadFile", var4);
            throw new RuntimeException(var4);
         }

         this.map.put(file, sm);
      }
   }

   private void loadDir(File dir) {
      Iterator i$ = FileUtils.listFiles(dir, new String[]{"xml"}, false).iterator();

      while(i$.hasNext()) {
         Object file = i$.next();
         this.loadFile((File)file);
      }

   }

   public void unload(File file) throws IllegalArgumentException {
      ScriptManager sm = (ScriptManager)this.map.remove(file);
      if (sm == null) {
         throw new IllegalArgumentException("ScriptManager by file " + file + " is not loaded.");
      } else {
         sm.shutdown();
      }
   }

   public void reload(File file) throws IllegalArgumentException {
      ScriptManager sm = (ScriptManager)this.map.get(file);
      if (sm == null) {
         throw new IllegalArgumentException("ScriptManager by file " + file + " is not loaded.");
      } else {
         sm.reload();
      }
   }

   public void addScriptManager(ScriptManager scriptManager, File file) {
      if (this.map.containsKey(file)) {
         throw new IllegalArgumentException("ScriptManager by file " + file + " is already loaded.");
      } else {
         this.map.put(file, scriptManager);
      }
   }

   public Map<File, ScriptManager> getLoadedScriptManagers() {
      return Collections.unmodifiableMap(this.map);
   }

   public void shutdown() {
      for(Iterator it = this.map.entrySet().iterator(); it.hasNext(); it.remove()) {
         try {
            ((ScriptManager)((Entry)it.next()).getValue()).shutdown();
         } catch (Exception var3) {
            log.warn("An exception occured during shudown procedure.", var3);
         }
      }

   }
}
