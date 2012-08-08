/*
 * JBoss, Home of Professional Open Source
 * Copyright 2011, Red Hat, Inc. and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */

package org.mobicents.slee.container.util.concurrent;

import java.util.AbstractSet;
import java.util.Collection;
import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;

/**
 * A simple Set implementation backed by a {@link java.util.concurrent.ConcurrentHashMap} replicated from JBoss Cache Core code by Manik Surtani. This almost 100% copy is just to don't depend on the the whole JBoss Cache.
 * 
 * @author martins
 */
public class ConcurrentHashSet<E> extends AbstractSet<E> {
   
   protected ConcurrentHashMap<E, Object> map;
   private static final Object DUMMY = new Object();

   public ConcurrentHashSet()
   {
      map = new ConcurrentHashMap<E, Object>();
   }

   @Override
   public int size()
   {
      return map.size();
   }

   @Override
   public boolean isEmpty()
   {
      return map.isEmpty();
   }

   @Override
   public boolean contains(Object o)
   {
      return map.containsKey(o);
   }

   @Override
   public Iterator<E> iterator()
   {
      return map.keySet().iterator();
   }

   @Override
   public Object[] toArray()
   {
      return map.keySet().toArray();
   }

   @Override
   public <T> T[] toArray(T[] a)
   {
      return map.keySet().toArray(a);
   }

   @Override
   public boolean add(E o)
   {
      Object v = map.put(o, DUMMY);
      return v == null;
   }

   @Override
   public boolean remove(Object o)
   {
      Object v = map.remove(o);
      return v != null;
   }

   @Override
   public boolean containsAll(Collection<?> c)
   {
      return map.keySet().containsAll(c);
   }

   @Override
   public boolean addAll(Collection<? extends E> c)
   {
      throw new UnsupportedOperationException("Not supported in this implementation since additional locking is required and cannot directly be delegated to multiple calls to ConcurrentHashMap");
   }

   @Override
   public boolean retainAll(Collection<?> c)
   {
      throw new UnsupportedOperationException("Not supported in this implementation since additional locking is required and cannot directly be delegated to multiple calls to ConcurrentHashMap");
   }

   @Override
   public boolean removeAll(Collection<?> c)
   {
      throw new UnsupportedOperationException("Not supported in this implementation since additional locking is required and cannot directly be delegated to multiple calls to ConcurrentHashMap");
   }

   @Override
   public void clear()
   {
      map.clear();
   }
}
