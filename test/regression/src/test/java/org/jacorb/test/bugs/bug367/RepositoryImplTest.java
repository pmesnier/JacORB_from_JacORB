/*
 *        JacORB  - a free Java ORB
 *
 *   Copyright (C) 1997-2014 Gerald Brose / The JacORB Team.
 *
 *   This library is free software; you can redistribute it and/or
 *   modify it under the terms of the GNU Library General Public
 *   License as published by the Free Software Foundation; either
 *   version 2 of the License, or (at your option) any later version.
 *
 *   This library is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 *   Library General Public License for more details.
 *
 *   You should have received a copy of the GNU Library General Public
 *   License along with this library; if not, write to the Free
 *   Software Foundation, Inc., 675 Mass Ave, Cambridge, MA 02139, USA.
 */

package org.jacorb.test.bugs.bug367;

import static org.junit.Assert.assertEquals;
import org.jacorb.ir.RepositoryImpl;
import org.junit.Test;

/**
 * @author Alphonse Bendt
 */
public class RepositoryImplTest
{
    @Test
    public void testId2ScopedName()
    {
        String result = RepositoryImpl.idToScopedName("IDL:org.jacorb.test/ir/StringAliasList:1.0", true);
        assertEquals("::org::jacorb::test::ir::StringAliasList", result);
    }
}
