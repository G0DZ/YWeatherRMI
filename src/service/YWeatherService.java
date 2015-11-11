package service;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface YWeatherService extends Remote
{
    String getStringWeather(int cityID) throws RemoteException;
}