package MainPackage;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

public class EnterForm extends JFrame {
	 
			client Client;
			JTextField loginField;
			JPasswordField passwordField;
			Box mainBox;
			
			
			EnterForm(){
				
				super("Вход в систему");
				Client = new client();
				setDefaultCloseOperation(EXIT_ON_CLOSE);
				
				// Настраиваем первую горизонтальную панель (для ввода логина)
				Box box1 = Box.createHorizontalBox();
				JLabel loginLabel = new JLabel("Логин:");
				loginField = new JTextField(15);
				box1.add(loginLabel);
				box1.add(Box.createHorizontalStrut(6));
				box1.add(loginField);
				
				
				// Настраиваем вторую горизонтальную панель (для ввода пароля)
				Box box2 = Box.createHorizontalBox();
				JLabel passwordLabel = new JLabel("Пароль:");
				passwordField = new JPasswordField(15);
				box2.add(passwordLabel);
				box2.add(Box.createHorizontalStrut(6));
				box2.add(passwordField);
				
				
				// Настраиваем третью горизонтальную панель (с кнопками)
				Box box3 = Box.createHorizontalBox();
				JButton ok = new JButton("OK");
				final JFrame thisFrame = this;
				ok.addActionListener(new ActionListener(){

					@Override
					public void actionPerformed(ActionEvent ae) {
						try {
							String result = "";
							if (loginField.getText().length() > 19 || passwordField.getText().length() > 19)
							{
								JOptionPane.showMessageDialog(new JFrame(), "Your login/password must have no more 20 symbols");
							} 
							else
							{
								result = Client.CheckUser(loginField.getText(), passwordField.getText());
								JOptionPane.showMessageDialog(new JFrame(), result, "Message", JOptionPane.DEFAULT_OPTION);
								
							}
							if (loginField.getText().equals("admin") && result.equals("Successfully entered."))
							{
								Client.getUserID(loginField.getText());
								Client.setUserName(loginField.getText());
								//JOptionPane.showMessageDialog(new JFrame(), Client.UserID, "Message", JOptionPane.DEFAULT_OPTION);
								new Cinema(Client, true);
								thisFrame.setVisible(false);
							} else
								if (result.equals("Successfully entered.") 
										|| result.equals("You are registred as " + loginField.getText() + ". Use this account for next enter."))
								{
									Client.getUserID(loginField.getText());
									Client.setUserName(loginField.getText());
									new Cinema(Client, false);
									thisFrame.setVisible(false);
								}
								else 
								{
									loginField.setText("");
									passwordField.setText("");
								}
									
							
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						
					}
					
				});
				JButton cancel = new JButton("Отмена");
				cancel.addActionListener(new ActionListener(){
					public void actionPerformed(ActionEvent ae)
					{
						dispose();
					}
				});
				box3.add(Box.createHorizontalGlue());
				box3.add(ok);
				box3.add(Box.createHorizontalStrut(12));
				box3.add(cancel);
				
				
				// Уточняем размеры компонентов
				loginLabel.setPreferredSize(passwordLabel.getPreferredSize());
				
				
				// Размещаем три горизонтальные панели на одной вертикальной
				mainBox = Box.createVerticalBox();
				mainBox.setBorder(new EmptyBorder(12,12,12,12));
				mainBox.add(box1);
				mainBox.add(Box.createVerticalStrut(12));
				mainBox.add(box2);
				mainBox.add(Box.createVerticalStrut(17));
				mainBox.add(box3);
				setContentPane(mainBox);
				pack();
				setResizable(false);
				this.setVisible(true);
			}
}
