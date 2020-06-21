package com.example.flightmobileapp

class Command (aileron: Double,elevator: Double, throttle: Double ,rudder: Double  ){
    private var aileron: Double =aileron
    private var elevator: Double = elevator
    private var throttle: Double = throttle
    private var rudder: Double = rudder

    fun setAileron(value: Double) {
        aileron = value
    }
    fun setElevator(value: Double) {
        elevator = value
    }
    fun setThrottle(value: Double) {
        throttle = value
    }
    fun setRudder(value: Double) {
        rudder = value
    }
}