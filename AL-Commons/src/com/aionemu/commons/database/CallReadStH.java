package com.aionemu.commons.database;

import java.sql.CallableStatement;
import java.sql.SQLException;

public interface CallReadStH extends ReadStH {
   void setParams(CallableStatement var1) throws SQLException;
}
