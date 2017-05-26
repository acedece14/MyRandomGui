package com.fedo;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.event.*;
import java.io.*;
import java.util.ArrayList;

import static com.fedo.RandomizerIFace.EMPTY_TEXT;

/**
 * //
 * Created by katz on 26.05.2017.
 */
public class MainFrame
        extends JFrame
        implements GuiIFace {

    private static final String LOGO_FILENAME = "logo-128.png";
    private JPanel pnlMain;
    private JButton btnAddItem;
    private JButton btnRemoveItem;
    private JList<String> lstItems;
    private JTextField edtitemName;
    private JLabel lblRezult;
    private JButton btnAddEmpty;
    private JButton btnClearItems;
    private JLabel lblStatistic;
    private JButton btnSaveToFile;
    private JComboBox<String> cbFilesToLoad;
    private JButton btnLoadFromFile;
    private JButton btnDeleteFile;
    private RandomizerIFace rmzer;

    MainFrame(RandomizerIFace randomizerIFace, String title) {

        this.rmzer = randomizerIFace;
        setTitle(title);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        initVisual();
        updateVisual();
        setContentPane(pnlMain);
        pack();
        setSize(500, 400);
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

        onUpdateFilesList();
        updateVisual();

        btnAddItem.addActionListener(e -> addItem());
        edtitemName.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                if (e.getKeyCode() == 10)
                    addItem();
            }
        });
        btnRemoveItem.addActionListener(e -> removeItem());
        btnAddEmpty.addActionListener(e -> {
            rmzer.addItem(EMPTY_TEXT);
            updateVisual();
        });
        btnClearItems.addActionListener(e -> {
            rmzer.clearItems();
            updateVisual();
        });
        btnSaveToFile.addActionListener(e -> {
            String name = JOptionPane.showInputDialog(this, "Enter name");
            rmzer.saveItemsToFile(this, name);
        });
        btnLoadFromFile.addActionListener(e -> {
            rmzer.loadItemsFromFile(this, (String) cbFilesToLoad.getSelectedItem());
        });
        btnDeleteFile.addActionListener(e -> {
            rmzer.deleteItemFile(this, (String) cbFilesToLoad.getSelectedItem());
        });

        lblRezult.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseReleased(MouseEvent e) {
                rmzer.startNewRandomMeassure(MainFrame.this);
            }
        });
        lstItems.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseReleased(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    removeItem();
                    return;
                }
                int index = lstItems.getSelectedIndex();
                if (index >= 0)
                    edtitemName.setText(lstItems.getModel().getElementAt(index));
            }
        });
    }

    private void removeItem() {

        if (lstItems.getModel().getSize() == 0 && lstItems.getSelectedIndex() < 0)
            return;
        for (int index : lstItems.getSelectedIndices()) {
            String item = lstItems.getModel().getElementAt(index);
            rmzer.removeItem(item);
        }
        updateVisual();
    }

    private void addItem() {
        if (edtitemName.getText() != null && edtitemName.getText().length() > 0)
            rmzer.addItem(edtitemName.getText());
        edtitemName.setText("");
        updateVisual();
    }

    private void updateVisual() {

        DefaultListModel<String> model = new DefaultListModel<>();
        ArrayList<String> items = (ArrayList<String>) rmzer.getItems();
        for (String itm : items)
            model.add(model.size(), itm);
        lstItems.setModel(model);
    }

    @Override
    public void onUpdateRandomResult(String rezult) {

        lblRezult.setText(rezult);
    }

    @Override
    public void onFinishRandomResult(String rezult) {

        lblStatistic.setText(rezult);
    }

    @Override
    public void onNoItemsToRandom() {

        lblRezult.setText("No items");
    }

    @Override
    public void onFewItemsToRandom() {

        lblRezult.setText("Few items");
    }

    @Override
    public void onUpdateFilesList() {
        Object selected = cbFilesToLoad.getSelectedItem();
        cbFilesToLoad.removeAllItems();
        for (String file : Utils.getStoreFilesList())
            cbFilesToLoad.addItem(file);
        if (selected != null)
            cbFilesToLoad.setSelectedItem(selected);
        updateVisual();
    }
}
