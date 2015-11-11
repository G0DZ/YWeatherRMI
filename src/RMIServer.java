import service.YWeatherRMI;
import service.YWeatherService;

import java.io.File;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class RMIServer {
    static Registry registry = null;
    private static int port = 9876;
    private static YWeatherService service = new YWeatherRMI();
    public static void main(String[] args) {
        System.out.println("Инициализация сервера... ");
        try {
            System.out.print("Запуск реестра RMI...");
            Registry registry = LocateRegistry.createRegistry(port);
            System.out.println(" ОК");
            Remote stub = UnicastRemoteObject.exportObject(service, 0);
            System.out.print("Привязка сервиса...");
            try {
                registry.bind("YWeatherService", stub);
            } catch (java.rmi.AlreadyBoundException abe) {
                System.out.println("Ошибка с регистрацией сервера AccountService");
                System.out.println("Данная ошибка критическая, поэтому программа аварийно завершилась");
            }
            System.out.println(" OK");
        } catch (RemoteException re) {
            System.out.println("Ошибка с запуском сервера RMI");
            System.out.println("Данная ошибка критическая, поэтому программа аварийно завершилась");
        }
        System.out.print("Проверка наличия деректории кэша... ");
        InitCache.initializeServer();
        System.out.println("Сервер запущен успешно!");
        while (true) {
            try {
                Thread.sleep(Integer.MAX_VALUE);
            }
            catch(InterruptedException ie) {
                System.out.println("Ошибка с потоками");
            }
        }
    }

    private static class InitCache {
        private static void initializeServer() {
            File cachepath = new File("cache/");
            if (!(cachepath.exists() && cachepath.isDirectory()))
            if (cachepath.mkdirs())
                System.out.println("Каталог создан!");
            else
                System.out.println("Ошибка при создании каталога кэша!");
        }
    }
}
