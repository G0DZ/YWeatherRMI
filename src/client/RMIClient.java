package client;

import service.Weather;
import service.YWeatherService;

import javax.swing.*;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.NotBoundException;
import java.sql.Time;
import java.util.ArrayList;
import java.util.StringTokenizer;

public class RMIClient implements Runnable{
    static String serverIP = "127.0.0.1";
    static int serverPort = 9876;
    static int cityID = 0;
    private Weather clientWeather = null;
    private ClientFrame parent;
    private Thread t;

    public RMIClient(String cityID, ClientFrame parent) {
        this.cityID = Integer.parseInt(cityID);
        this.parent = parent;
        t = new Thread(this);
        t.start();
    }

    @Override
    public void run() {
        String result = null;
        System.out.println("Инициализация клиента... ");
        try {
            Registry registry = LocateRegistry.getRegistry(serverIP, serverPort);
            try {
                YWeatherService service = (YWeatherService) registry.lookup("YWeatherService");
                result = service.getStringWeather(cityID);
            }
            catch(NotBoundException fe) {
                System.out.println("Ошибка регистра. Не найден AccountService");
                System.out.println("Данная ошибка критическая, поэтому программа аварийно завершилась");
            }
        }
        catch(RemoteException re) {
            System.out.println("Ошибка с подключением rmi к серверу");
            System.out.println("Данная ошибка критическая, поэтому программа аварийно завершилась");
        }
        System.out.println("Ответ сервера получен, обработка.");
        if(result != null) { //строка пуста, например, если нет интернета.
            //в таком случае, сообщение об ошибке уже было, а значит нужно просто обработать
            //успешный случай.
            System.out.println(result);
            ArrayList<String> list = splitAnswer(result);
            clientWeather = parseAnswer(list);
            if (clientWeather != null) {
                //присвоение значений элементов форм
                //согласно полученным полям
                parent.cityName.setText(clientWeather.city + ", " + clientWeather.country);
                parent.timeLabel.setText(new Time(System.currentTimeMillis()).toString());
                parent.nowLabel.setText("Время обновления: ");
                ImageIcon A = new ImageIcon(getClass().getClassLoader().getResource("images/" +
                        clientWeather.imageName + ".png"));
                parent.weatherImage.setIcon(new ImageIcon(getClass().getClassLoader().getResource("images/" +
                        clientWeather.imageName + ".png")));
                if (Integer.parseInt(clientWeather.temperature) > 0)
                    parent.temperatureLabel.setText("+" + clientWeather.temperature + " \u00b0" + "C");
                else
                    parent.temperatureLabel.setText(clientWeather.temperature + " \u00b0" + "C");
                parent.weatherTypeText.setText(clientWeather.weatherType);
                parent.windText.setText(", ветер "+clientWeather.windSpeed+" м/с");
            }
        }
    }

    public static ArrayList<String> splitAnswer(String str){
        //str = str.replace("","");
        StringTokenizer stk = new StringTokenizer(str,"$"); //логические отрезки разделены запятыми
        int z = stk.countTokens(); //количество разделителей в строке
        ArrayList<String> list = new ArrayList<>();
        //System.out.println("Тестирование метода parseAnswer\n");
        for(int i = 0; i<z; i++)
        {
            list.add(stk.nextToken());
            //System.out.println(list.get(i));
        }
        return list;
    }

    private static Weather parseAnswer(ArrayList<String> list){
        String str = null;
        Weather w = new Weather();
        str = list.get(0);
        w.city = str.substring(5,str.length());	//city = TEST
        str = list.get(1);
        w.country = str.substring(8,str.length());	//country = TEST
        str = list.get(2);
        w.time = str.substring(5,str.length());	//time = TEST
        str = list.get(3);
        w.temperature = str.substring(12,str.length());	//temperature = TEST
        str = list.get(4);
        w.weatherType = str.substring(12,str.length());	//weatherType = TEST
        str = list.get(5);
        w.windDirection = str.substring(14,str.length());	//windDirection = TEST
        str = list.get(6);
        w.windSpeed = str.substring(10,str.length());	//windSpeed = TEST
        str = list.get(7);
        w.humidity = str.substring(9,str.length());	//humidity = TEST
        str = list.get(8);
        w.pressure = str.substring(9,str.length());	//pressure = TEST
        str = list.get(9);
        w.imageName = str.substring(10,str.length());//imageName=TEST
        //System.out.println("HELLO\n\n"+w.toString());
        return w;
    }
}