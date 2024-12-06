/**
 * This file is part of Aion-Lightning <aion-lightning.org>.
 *
 * Aion-Lightning is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 *
 * Aion-Lightning is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details. *
 *
 * You should have received a copy of the GNU General Public License along with Aion-Lightning. If not, see <http://www.gnu.org/licenses/>.
 *
 *
 * Credits goes to all Open Source Core Developer Groups listed below Please do not change here something, ragarding the developer credits, except the
 * "developed by XXXX". Even if you edit a lot of files in this source, you still have no rights to call it as "your Core". Everybody knows that this
 * Emulator Core was developed by Aion Lightning
 * 
 * @-Aion-Unique-
 * @-Aion-Lightning
 * @Aion-Engine
 * @Aion-Extreme
 * @Aion-NextGen
 * @Aion-Core Dev.
 */
package com.aionemu.commons.utils.internal.chmv8;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.nio.Buffer;
import java.nio.ByteBuffer;

import com.aionemu.commons.utils.SystemPropertyUtil;

import sun.misc.Cleaner;
import sun.misc.Unsafe;

/**
 * The {@link PlatformDependent} operations which requires access to {@code sun.misc.*}.
 */
final class PlatformDependent0 {
	
	private static final Unsafe UNSAFE;
	private static final long CLEANER_FIELD_OFFSET;
	private static final long ADDRESS_FIELD_OFFSET;
	/**
	 * {@code true} if and only if the platform supports unaligned access.
	 *
	 * @see <a href= "http://en.wikipedia.org/wiki/Segmentation_fault#Bus_error">Wikipedia on segfault</a>
	 */
	private static final boolean UNALIGNED;
	
	static {
		ByteBuffer direct = ByteBuffer.allocateDirect(1);
		Field cleanerField;
		try {
			cleanerField = direct.getClass().getDeclaredField("cleaner");
			cleanerField.setAccessible(true);
			Cleaner cleaner = (Cleaner) cleanerField.get(direct);
			cleaner.clean();
		} catch (Throwable t) {
			cleanerField = null;
		}
		
		Field addressField;
		try {
			addressField = Buffer.class.getDeclaredField("address");
			addressField.setAccessible(true);
			if (addressField.getLong(ByteBuffer.allocate(1)) != 0) {
				addressField = null;
			} else {
				direct = ByteBuffer.allocateDirect(1);
				if (addressField.getLong(direct) == 0) {
					addressField = null;
				}
				Cleaner cleaner = (Cleaner) cleanerField.get(direct);
				cleaner.clean();
			}
		} catch (Throwable t) {
			addressField = null;
		}
		
		Unsafe unsafe;
		if (addressField != null && cleanerField != null) {
			try {
				Field unsafeField = Unsafe.class.getDeclaredField("theUnsafe");
				unsafeField.setAccessible(true);
				unsafe = (Unsafe) unsafeField.get(null);
				
				// Ensure the unsafe supports all necessary methods to work
				// around the mistake in the latest OpenJDK.
				// http://www.mail-archive.com/jdk6-dev@openjdk.java.net/msg00698.html
				unsafe.getClass().getDeclaredMethod("copyMemory", new Class[] {Object.class, long.class, Object.class, long.class, long.class});
			} catch (Throwable cause) {
				unsafe = null;
			}
		} else {
			// If we cannot access the address of a direct buffer, there's no
			// point of using unsafe.
			// Let's just pretend unsafe is unavailable for overall simplicity.
			unsafe = null;
		}
		UNSAFE = unsafe;
		
		if (unsafe == null) {
			CLEANER_FIELD_OFFSET = -1;
			ADDRESS_FIELD_OFFSET = -1;
			UNALIGNED = false;
		} else {
			ADDRESS_FIELD_OFFSET = objectFieldOffset(addressField);
			CLEANER_FIELD_OFFSET = objectFieldOffset(cleanerField);
			
			boolean unaligned;
			try {
				Class<?> bitsClass = Class.forName("java.nio.Bits", false, ClassLoader.getSystemClassLoader());
				Method unalignedMethod = bitsClass.getDeclaredMethod("unaligned");
				unalignedMethod.setAccessible(true);
				unaligned = Boolean.TRUE.equals(unalignedMethod.invoke(null));
			} catch (Throwable t) {
				// We at least know x86 and x64 support unaligned access.
				String arch = SystemPropertyUtil.get("os.arch", "");
				// noinspection DynamicRegexReplaceableByCompiledPattern
				unaligned = arch.matches("^(i[3-6]86|x86(_64)?|x64|amd64)$");
			}
			UNALIGNED = unaligned;
		}
	}
	
	static boolean hasUnsafe() {
		return UNSAFE != null;
	}
	
	static void throwException(Throwable t) {
		UNSAFE.throwException(t);
	}
	
	static void freeDirectBuffer(ByteBuffer buffer) {
		Cleaner cleaner;
		try {
			cleaner = (Cleaner) getObject(buffer, CLEANER_FIELD_OFFSET);
			if (cleaner == null) {
				throw new IllegalArgumentException("attempted to deallocate the buffer which was allocated via JNIEnv->NewDirectByteBuffer()");
			}
			cleaner.clean();
		} catch (Throwable t) {
			// Nothing we can do here.
		}
	}
	
	static long directBufferAddress(ByteBuffer buffer) {
		return getLong(buffer, ADDRESS_FIELD_OFFSET);
	}
	
	static long arrayBaseOffset() {
		return UNSAFE.arrayBaseOffset(byte[].class);
	}
	
	static Object getObject(Object object, long fieldOffset) {
		return UNSAFE.getObject(object, fieldOffset);
	}
	
	static int getInt(Object object, long fieldOffset) {
		return UNSAFE.getInt(object, fieldOffset);
	}
	
	private static long getLong(Object object, long fieldOffset) {
		return UNSAFE.getLong(object, fieldOffset);
	}
	
	static long objectFieldOffset(Field field) {
		return UNSAFE.objectFieldOffset(field);
	}
	
	static byte getByte(long address) {
		return UNSAFE.getByte(address);
	}
	
	static short getShort(long address) {
		if (UNALIGNED) {
			return UNSAFE.getShort(address);
		} else {
			return (short) (getByte(address) << 8 | getByte(address + 1) & 0xff);
		}
	}
	
	static int getInt(long address) {
		if (UNALIGNED) {
			return UNSAFE.getInt(address);
		} else {
			return getByte(address) << 24 | (getByte(address + 1) & 0xff) << 16 | (getByte(address + 2) & 0xff) << 8 | getByte(address + 3) & 0xff;
		}
	}
	
	static long getLong(long address) {
		if (UNALIGNED) {
			return UNSAFE.getLong(address);
		} else {
			return (long) getByte(address) << 56 | ((long) getByte(address + 1) & 0xff) << 48 | ((long) getByte(address + 2) & 0xff) << 40 | ((long) getByte(address + 3) & 0xff) << 32
					| ((long) getByte(address + 4) & 0xff) << 24 | ((long) getByte(address + 5) & 0xff) << 16 | ((long) getByte(address + 6) & 0xff) << 8 | (long) getByte(address + 7) & 0xff;
		}
	}
	
	static void putByte(long address, byte value) {
		UNSAFE.putByte(address, value);
	}
	
	static void putShort(long address, short value) {
		if (UNALIGNED) {
			UNSAFE.putShort(address, value);
		} else {
			putByte(address, (byte) (value >>> 8));
			putByte(address + 1, (byte) value);
		}
	}
	
	static void putInt(long address, int value) {
		if (UNALIGNED) {
			UNSAFE.putInt(address, value);
		} else {
			putByte(address, (byte) (value >>> 24));
			putByte(address + 1, (byte) (value >>> 16));
			putByte(address + 2, (byte) (value >>> 8));
			putByte(address + 3, (byte) value);
		}
	}
	
	static void putLong(long address, long value) {
		if (UNALIGNED) {
			UNSAFE.putLong(address, value);
		} else {
			putByte(address, (byte) (value >>> 56));
			putByte(address + 1, (byte) (value >>> 48));
			putByte(address + 2, (byte) (value >>> 40));
			putByte(address + 3, (byte) (value >>> 32));
			putByte(address + 4, (byte) (value >>> 24));
			putByte(address + 5, (byte) (value >>> 16));
			putByte(address + 6, (byte) (value >>> 8));
			putByte(address + 7, (byte) value);
		}
	}
	
	static void copyMemory(long srcAddr, long dstAddr, long length) {
		UNSAFE.copyMemory(srcAddr, dstAddr, length);
	}
	
	static void copyMemory(Object src, long srcOffset, Object dst, long dstOffset, long length) {
		UNSAFE.copyMemory(src, srcOffset, dst, dstOffset, length);
	}
	
	private PlatformDependent0() {}
}
