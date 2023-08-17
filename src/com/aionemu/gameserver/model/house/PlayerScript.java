/*
 * This file is part of Encom. **ENCOM FUCK OTHER SVN**
 *
 *  Encom is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU Lesser Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  Encom is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU Lesser Public License for more details.
 *
 *  You should have received a copy of the GNU Lesser Public License
 *  along with Encom.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.aionemu.gameserver.model.house;

import com.aionemu.commons.taskmanager.AbstractLockManager;

public final class PlayerScript extends AbstractLockManager
{
    public PlayerScript() {
    }
	
    public PlayerScript(byte[] compressedBytes, int uncompressedSize) {
        this.compressedBytes = compressedBytes;
        this.uncompressedSize = uncompressedSize;
    }
	
    private int uncompressedSize = -1;
    private byte[] compressedBytes = null;
	
    public int getUncompressedSize() {
        return uncompressedSize;
    }
	
    public byte[] getCompressedBytes() {
        return compressedBytes;
    }
	
    public void setData(byte[] compressedBytes, int uncompressedSize) {
        writeLock();
        this.compressedBytes = compressedBytes;
        this.uncompressedSize = uncompressedSize;
        writeUnlock();
    }
}