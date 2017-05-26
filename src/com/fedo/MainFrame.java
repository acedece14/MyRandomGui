package com.fedo;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.fedo.RandomizerIFace.EMPTY_TEXT;

/**
 * //
 * Created by katz on 26.05.2017.
 */
public class MainFrame extends JFrame {
    private static final String LOGO_FILENAME = "logo-128.png";
    private JPanel pnlMain;
    private JButton btnAddItem;
    private JButton btnRemoveItem;
    private JList<String> lstItems;
    private JTextField edtitemName;
    private JLabel lblRezult;
    private JButton btnAddEmpty;
    private JButton btnClearItems;
    private JLabel lblRezults;
    private RandomizerIFace rmzer;

    MainFrame(RandomizerIFace randomizerIFace, String title) {

        this.rmzer = randomizerIFace;
        setTitle(title);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        initVisual();
        updateVisual();
        setContentPane(pnlMain);
        pack();
        setSize(400, 300);
        setLocationRelativeTo(null);
        setVisible(true);
    }


    private void initVisual() {

        try {
            BufferedInputStream bis = new BufferedInputStream(getClass().getClassLoader()
                    .getResourceAsStream(LOGO_FILENAME));
            setIconImage(ImageIO.read(bis));
        } catch (IOException e) {
            e.printStackTrace();
        }


        btnAddItem.addActionListener(e -> {
            if (edtitemName.getText() != null && edtitemName.getText().length() > 0)
                rmzer.addItem(edtitemName.getText());
            edtitemName.setText("");
            updateVisual();
        });
        btnRemoveItem.addActionListener(e -> {
            if (lstItems.getModel().getSize() == 0 && lstItems.getSelectedIndex() < 0)
                return;
            int[] indexes = lstItems.getSelectedIndices();
            for (int index : indexes) {
                String item = lstItems.getModel().getElementAt(index);
                rmzer.removeItem(item);
            }
            updateVisual();
        });
        btnAddEmpty.addActionListener(e -> {
            rmzer.addItem(EMPTY_TEXT);
            updateVisual();
        });
        btnClearItems.addActionListener(e -> {
            rmzer.clearItems();
            updateVisual();
        });
        lblRezult.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseReleased(MouseEvent e) {

                new Thread(() -> {
                    int count = 500;
                    // value count
                    Map<String, Integer> map = new HashMap<>();
                    while (count-- > 0 || rmzer.getRandom().equals(EMPTY_TEXT)) {
                        lblRezult.setText(rmzer.getNextRandom());
                        if (map.containsKey(rmzer.getRandom()))
                            map.put(rmzer.getRandom(), map.get(rmzer.getRandom()) + 1);
                        else if (!rmzer.getRandom().equals(EMPTY_TEXT))
                            map.put(rmzer.getRandom(), 1);
                        try {
                            Thread.sleep(2);
                        } catch (InterruptedException ignored) {
                        }
                    }
                    StringBuilder rezult = new StringBuilder();
                    for (Map.Entry<String, Integer> entry : map.entrySet()) {
                        String key = entry.getKey();
                        Integer value = entry.getValue();
                        rezult.append(String.format("%s %d<br>", key, value));
                    }
                    rezult.insert(0, "<html>");
                    rezult.append("</html>");
                    lblRezults.setText(rezult.toString());
                }).start();
            }
        });
        lstItems.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseReleased(MouseEvent e) {
                int index = lstItems.getSelectedIndex();
                if (index >= 0)
                    edtitemName.setText(lstItems.getModel().getElementAt(index));
            }
        });
    }

    private void updateVisual() {

        DefaultListModel<String> model = new DefaultListModel<>();
        ArrayList<String> items = (ArrayList<String>) rmzer.getItems();
        for (String itm : items)
            model.add(model.size(), itm);
        lstItems.setModel(model);
    }

}
