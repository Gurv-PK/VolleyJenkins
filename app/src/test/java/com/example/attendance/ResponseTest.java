package com.example.attendance;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


@RunWith(MockitoJUnitRunner.class)
public class ResponseTest {

    public AttendancePojo attendancePojo;

    public ResponseTest() {

    }



    @InjectMocks
    TestInterface ti = new TestInterface();
    AllUser au = new AllUser();

    @Mock
    DBHelper helper;

    @Test
    public void testInsert(){
        when(helper.insert(String.valueOf(19031),"2UnitedTest")).thenReturn(true);
        Assert.assertEquals(ti.testinsert(String.valueOf(19031),"2UnitedTest"),true);
    }

    @Test
    public void testMark(){
        when(helper.mark(String.valueOf(19031),"2/02/2021","Present","09:21:20","07:15:19")).thenReturn(true);
        Assert.assertEquals(ti.testmark(String.valueOf(19031),"2/02/2021","Present","09:21:20","07:15:19"),true);
    }

    @Test
    public void testFetchStatus(){
        when(helper.Fetch_recent_status(String.valueOf(19031),"2/02/2021")).thenReturn("Present");
        Assert.assertEquals(ti.testfetchstatus(String.valueOf(19031),"2/02/2021"),"Present");
    }

    @Test
    public void testFetchCheckIN(){
        when(helper.Fetch_Check_IN(String.valueOf(19031),"2/02/2021")).thenReturn("Present");
        Assert.assertEquals(ti.testfetchcheckin(String.valueOf(19031),"2/02/2021"),"Present");
    }

    @Test
    public void testFetchCheckOut(){
        when(helper.Fetch_Check_out(String.valueOf(19031),"2/02/2021")).thenReturn("Present");
        Assert.assertEquals(ti.testfetchcheckout(String.valueOf(19031),"2/02/2021"),"Present");
    }

//    @Test
//    public void verifySetData()
//    {
//
//
//    }

}
