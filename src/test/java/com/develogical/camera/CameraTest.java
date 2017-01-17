package com.develogical.camera;

import org.jmock.Expectations;
import org.jmock.integration.junit4.JMock;
import org.jmock.integration.junit4.JUnitRuleMockery;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.Mockito.*;

public class CameraTest
{

    @Rule
    public JUnitRuleMockery context = new JUnitRuleMockery();

    private MemoryCard mockMemoryCard()
    {
        return context.mock(MemoryCard.class);
    }

    private Sensor mockSensor()
    {
        return context.mock(Sensor.class);
    }

    @Test
    public void switchingTheCameraOnPowersUpTheSensor()
    {
        Sensor sensor = mockSensor();
        Camera camera = new Camera(mockMemoryCard(), sensor);

        context.checking(new Expectations() {{
            exactly(1).of(sensor).powerUp();
        }});

        camera.powerOn();

        // Alternate Mockito implementation - for reference
//        MemoryCard memoryCard = Mockito.mock(MemoryCard.class);
//        Sensor sensor = Mockito.mock(Sensor.class);
//
//        Camera camera = new Camera(memoryCard, sensor);
//
//        camera.powerOn();
//
//        Mockito.verify(sensor, Mockito.atLeastOnce()).powerUp();
    }

    @Test
    public void switchingTheCameraOffPowersDownTheSensor()
    {
        Sensor sensor = mockSensor();
        Camera camera = new Camera(mockMemoryCard(), sensor);

        context.checking(new Expectations() {{
            exactly(1).of(sensor).powerDown();
        }});

        camera.powerOff();
    }

    @Test
    public void pressingTheShutterWhenThePowerIsOffDoesNothing()
    {
        MemoryCard memoryCard = mockMemoryCard();
        Sensor sensor = mockSensor();
        Camera camera = new Camera(memoryCard, sensor);

        context.checking(new Expectations() {{
            never(memoryCard);
            allowing(sensor).powerDown();
            never(sensor).readData();
        }});

        camera.powerOff();

        camera.pressShutter();

    }

    @Test
    public void pressingTheShutterWhenThePowerIsOnCopiesData()
    {
        MemoryCard memoryCard = mockMemoryCard();
        Sensor sensor = mockSensor();
        Camera camera = new Camera(memoryCard, sensor);

        context.checking(new Expectations()
        {{
            allowing(sensor).powerUp();

            byte[] expected = new byte[0];
            allowing(sensor).readData();
                will(returnValue(expected));
            exactly(1).of(memoryCard).write(expected);
        }});

        camera.powerOn();
        camera.pressShutter();
    }

    @Test
    public void pressingThePowerOffWhileDataCopy()
    {
        Sensor sensor = mockSensor();
        Camera camera = new Camera(mockMemoryCard(), sensor);

        context.checking(new Expectations()
        {{
            camera.writeStarting();
            never(sensor).powerDown();
        }});

        camera.powerOff();

    }

    @Test
    public void writeCompletingPowersDownSensor()
    {
        Sensor sensor = mockSensor();
        Camera camera = new Camera(mockMemoryCard(), sensor);

        context.checking(new Expectations()
        {{
            exactly(1).of(sensor).powerDown();
        }});

        camera.writeComplete();
    }

}
