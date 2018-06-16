// Copyright (c) 2003-present, Jodd Team (http://jodd.org)
// All rights reserved.
//
// Redistribution and use in source and binary forms, with or without
// modification, are permitted provided that the following conditions are met:
//
// 1. Redistributions of source code must retain the above copyright notice,
// this list of conditions and the following disclaimer.
//
// 2. Redistributions in binary form must reproduce the above copyright
// notice, this list of conditions and the following disclaimer in the
// documentation and/or other materials provided with the distribution.
//
// THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
// AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
// IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
// ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE
// LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
// CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
// SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
// INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
// CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
// ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
// POSSIBILITY OF SUCH DAMAGE.

package jodd.buffer;

import java.util.Arrays;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.RandomAccess;

/**
 * Faster Objects buffer.
 */
@SuppressWarnings("unchecked")
public class FastBuffer<E> implements RandomAccess, Iterable<E> {

	private E[] buffer;
	private int offset;

	/**
	 * Creates a new {@code byte} buffer. The buffer capacity is
	 * initially 64 bytes, though its size increases if necessary.
	 */
	public FastBuffer() {
		this.buffer = (E[]) new Object[64];
	}

	/**
	 * Creates a new {@code byte} buffer, with a buffer capacity of
	 * the specified size.
	 *
	 * @param size the initial size.
	 * @throws IllegalArgumentException if size is negative.
	 */
	public FastBuffer(final int size) {
		this.buffer = (E[]) new Object[size];
	}

	/**
	 * Grows the buffer.
	 */
	private void grow(final int minCapacity) {
		final int oldCapacity = buffer.length;
		int newCapacity = oldCapacity << 1;
		if (newCapacity - minCapacity < 0) {
			// special case, min capacity is larger then a grow
			newCapacity = minCapacity + 512;
		}
		buffer = Arrays.copyOf(buffer, newCapacity);
	}

	/**
	 * Appends single {@code byte} to buffer.
	 */
	public void append(final E element) {
		if (offset - buffer.length >= 0) {
			grow(offset);
		}

		buffer[offset++] = element;
	}

	/**
	 * Appends {@code byte} array to buffer.
	 */
	public FastBuffer append(final E[] array, final int off, final int len) {
		if (offset + len - buffer.length > 0) {
			grow(offset + len);
		}

		System.arraycopy(array, off, buffer, offset, len);
		offset += len;
		return this;
	}

	/**
	 * Appends {@code byte} array to buffer.
	 */
	public FastBuffer append(final E[] array) {
		return append(array, 0, array.length);
	}

	/**
	 * Appends another fast buffer to this one.
	 */
	public FastBuffer append(final FastBuffer<E> buff) {
		if (buff.offset == 0) {
			return this;
		}
		append(buff.buffer, 0, buff.offset);
		return this;
	}

	/**
	 * Returns buffer size.
	 */
	public int size() {
		return offset;
	}

	/**
	 * Tests if this buffer has no elements.
	 */
	public boolean isEmpty() {
		return offset == 0;
	}

	/**
	 * Resets the buffer content.
	 */
	public void clear() {
		offset = 0;
	}

	/**
	 * Creates {@code byte} array from buffered content.
	 */
	public E[] toArray() {
		return Arrays.copyOf(buffer, offset);
	}

	/**
	 * Creates {@code byte} subarray from buffered content.
	 */
	public E[] toArray(final int start, final int len) {
		final Object[] array = new Object[len];

		if (len == 0) {
			return (E[]) array;
		}

		System.arraycopy(buffer, start, array, 0, len);

		return (E[]) array;
	}

	/**
	 * Returns {@code byte} element at given index.
	 */
	public E get(final int index) {
		if (index >= offset) {
			throw new IndexOutOfBoundsException();
		}
		return buffer[index];
	}

	/**
	 * Adds element to buffer.
	 */
	public void add(final E element) {
		append(element);
	}

	/**
	 * Returns an iterator over buffer elements.
	 */
	@Override
	public Iterator<E> iterator() {
		return new Iterator<E>() {
			int iteratorIndex;

			@Override
			public boolean hasNext() {
				return iteratorIndex < offset;
			}

			@Override
			public E next() {
				if (iteratorIndex >= offset) {
					throw new NoSuchElementException();
				}
				final E result = buffer[iteratorIndex];

				iteratorIndex++;

				return result;
			}

			@Override
			public void remove() {
				throw new UnsupportedOperationException();
			}
		};
	}
}