package com.develogical.camera;

public class Camera implements WriteListener {
    private MemoryCard memoryCard;

    private Sensor sensor;

    private boolean PoweredOn = false;
    private boolean writing = false;

    public Camera(MemoryCard memoryCard,
                  Sensor sensor)
    {
        this.memoryCard = memoryCard;
        this.sensor = sensor;
    }

    public void pressShutter() {
        // not implemented
        if (PoweredOn) {
        memoryCard.write(sensor.readData());
        }
    }

    public void powerOn() {
        sensor.powerUp();
        PoweredOn = true;
    }

    public void powerOff() {
        if (!writing) {
            sensor.powerDown();
        }
        PoweredOn = false;
    }

    @Override
    public void writeStarting()
    {
        writing = true;
    }

    @Override
    public void writeComplete()
    {
        writing = false;
        sensor.powerDown();
    }
}

