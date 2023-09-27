/*

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
package com.aionemu.gameserver.taskmanager.parallel;

import com.aionemu.commons.utils.internal.chmv8.CountedCompleter;
import com.aionemu.commons.utils.internal.chmv8.ForkJoinTask;
import com.google.common.base.Predicate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;

public final class ForEach<E> extends CountedCompleter<E>
{
	private static final Logger log = LoggerFactory.getLogger(ForEach.class);
	private static final long serialVersionUID = 7902148320917998146L;
	
	public static <E> ForkJoinTask<E> forEach(Collection<E> list, Predicate<E> operation) {
		if (list.size() > 0) {
			@SuppressWarnings("unchecked")
			E[] objects = list.toArray((E[]) new Object[list.size()]);
			CountedCompleter<E> completer = new ForEach<E>(null, operation, 0, objects.length, objects);
			return completer;
		}
		return null;
	}
	
	public static <E> ForkJoinTask<E> forEach(Predicate<E> operation, E... list) {
		if (list != null && list.length > 0) {
			CountedCompleter<E> completer = new ForEach<E>(null, operation, 0, list.length, list);
			return completer;
		}
		return null;
	}
	
	final E[] list;
	final Predicate<E> operation;
	final int lo, hi;
	
	private ForEach(CountedCompleter<E> rootTask, Predicate<E> operation, int lo, int hi, E... list) {
		super(rootTask);
		this.list = list;
		this.operation = operation;
		this.lo = lo;
		this.hi = hi;
	}
	
	@Override
	public void compute() {
		int l = lo, h = hi;
		while (h - l >= 2) {
			int mid = (l + h) >>> 1;
			addToPendingCount(1);
			new ForEach<E>(this, operation, mid, h, list).fork();
			h = mid;
		} if (h > l) {
			try {
				operation.apply(list[l]);
			} catch (Throwable ex) {
				onExceptionalCompletion(ex, this);
			}
		}
		propagateCompletion();
	}
	
	@Override
	public boolean onExceptionalCompletion(Throwable ex, CountedCompleter<?> caller) {
		//log.warn("", ex);
		return true;
	}
}