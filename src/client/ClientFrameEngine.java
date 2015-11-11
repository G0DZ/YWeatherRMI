package client;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JOptionPane;

public class ClientFrameEngine implements ActionListener{
    private ClientFrame parent;
    private String cityID;

    public ClientFrameEngine(ClientFrame parent) {
        this.parent = parent;
    }
    @Override
    public void actionPerformed(ActionEvent arg0) {
        JButton clickedButton =  (JButton) arg0.getSource();
        // Если это кнопка "Ввод исходных данных"
        String actioncommand = clickedButton.getActionCommand();
        if (actioncommand.equals(ClientFrame.button_start.getText())){
            cityID = parent.textField.getText();
            if(!cityID.equals("")){
                synchronized (new RMIClient(cityID, parent)){} //синхронизированный поток для расчета информации
            }
            else{
                JOptionPane.showMessageDialog(null, "Строка ввода города пуста!");
            }
        }
    }
}