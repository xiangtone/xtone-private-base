// Decompiled by DJ v2.9.9.61 Copyright 2000 Atanas Neshkov  Date: 2003-1-22 10:15:17
// Home Page : http://members.fortunecity.com/neshkov/dj.html  - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   cmppe_cancel.java

package comsd.commerceware.cmpp;


// Referenced classes of package com.commerceware.cmpp:
//            OutOfBoundsException

public final class cmppe_cancel
{

    public cmppe_cancel()
    {
        msg_id = new byte[8];
    }

    public void set_msgid(String msgid)
        throws OutOfBoundsException
    {
        OutOfBoundsException e = new OutOfBoundsException();
        int len = msgid.length();
        if(len > 64)
            throw e;
        int j;
        for(j = 0; j < len; j++)
            msg_id[j] = (byte)msgid.charAt(j);

        msg_id[j] = 0;
    }

    protected byte msg_id[];
}