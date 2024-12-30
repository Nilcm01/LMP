package cat.nilcm01.screens;

import cat.nilcm01.Album;
import cat.nilcm01.AlbumVariant;
import cat.nilcm01.Artist;
import cat.nilcm01.Library;
import cat.nilcm01.utils.DirectoryManagement;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.Objects;

public class Viewer extends JFrame {
    private JPanel contentPane;
    private JScrollPane scrollPaneLibraryArtists;
    private JLabel labelLibrary;
    private JList<Object> listLibraryArtists;
    private JScrollPane scrollPaneLibraryAlbums;
    private JList<Object> listLibraryAlbums;
    private JPanel scrollPaneLibraryVariants;
    private JButton buttonSendToDevice;
    private JButton buttonDeleteFromDevice;
    private JList<Object> listLibraryVariants;
    private JLabel labelStatusLibrary;
    private JLabel labelStatusDevice;
    private JProgressBar progressAction;
    private JLabel progressActionArtist;
    private JLabel progressActionAlbum;
    private JLabel progressActionVariant;
    private JLabel progressActionType;
    private JLabel progressActionResult;


    private Library library;
    private Boolean libraryLastStatus = false;
    private Library device;
    private Boolean deviceLastStatus = false;


    public Viewer(String pathLibrary, String pathDevice) {
        JFrame frame = new JFrame("");
        setTitle("LMP - Llibreria Musical Portàtil");
        setContentPane(contentPane);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //pack();
        setSize(1280, 720);
        setLocationRelativeTo(null);

        // Set custom renderer for the lists
        listLibraryArtists.setCellRenderer(artistListRenderer);
        listLibraryAlbums.setCellRenderer(albumListRenderer);
        listLibraryVariants.setCellRenderer(variantListRenderer);

        //// START OF FRAME ////

        loadLibraries(pathLibrary, pathDevice);
        updateLibraryArtists();

        listLibraryArtists.addListSelectionListener(e -> updateLibraryAlbumList());
        listLibraryAlbums.addListSelectionListener(e -> updateLibraryVariants());

        buttonSendToDevice.addActionListener(e -> sendButton());
        buttonDeleteFromDevice.addActionListener(e -> deleteButton());

        // Call updateStatus() every 5 seconds
        updateStatus();
        Timer timer = new Timer(5000, e -> updateStatus());
        timer.start();

        //// END OF FRAME ////
        setVisible(true);
    }

    private void loadLibraries(String pathLibrary, String pathDevice) {
        library = new Library(pathLibrary);
        device = new Library(pathDevice);
    }

    private void updateLibraryArtists() {
        //List<String> artistsLibrary = library.getArtistsNames();
        List<Artist> artistsLibrary = library.getArtists();
        if (artistsLibrary != null) {
            artistsLibrary.sort((a1, a2) -> a1.getName().compareToIgnoreCase(a2.getName()));
            listLibraryArtists.setListData(artistsLibrary.toArray());
        }
    }

    private void updateLibraryAlbumList() {
        // Get the selected artist
        Artist selectedArtist = (Artist) listLibraryArtists.getSelectedValue();
        if (selectedArtist == null) {
            listLibraryAlbums.setListData(new Object[0]);
            return;
        }

        // Get the albums of the selected artist
        List<Album> albums = selectedArtist.getAlbums();
        if (albums != null) {
            albums.sort((a1, a2) -> a1.getTitle().compareToIgnoreCase(a2.getTitle()));
            listLibraryAlbums.setListData(albums.toArray());
        }
    }

    private void updateLibraryVariants() {
        // Get the selected album
        Album selectedAlbum = (Album) listLibraryAlbums.getSelectedValue();
        if (selectedAlbum == null) {
            listLibraryVariants.setListData(new Object[0]);
            return;
        }

        // Get artist
        Artist artist = library.getArtist(selectedAlbum.getArtist());
        if (artist == null) return;

        // Get the variants of the selected album
        Album album = artist.getAlbum(selectedAlbum.getTitle());
        if (album == null) return;

        // Get the variants
        List<AlbumVariant> variants = album.getVariants();
        if (variants == null) return;

        // Populate the selector
        variants.sort((v1, v2) -> {
            String format1 = v1.getFormat();
            String format2 = v2.getFormat();
            if (!Objects.equals(format1, format2)) return format1.compareToIgnoreCase(format2);
            int bits1 = v1.getBits();
            int bits2 = v2.getBits();
            if (bits1 != bits2) return bits1 - bits2;
            return v1.getSample() - v2.getSample();
        });
        listLibraryVariants.setListData(variants.toArray());
    }

    ListCellRenderer<Object> artistListRenderer = new DefaultListCellRenderer() {
        @Override
        public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
            Component c = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
            if (c instanceof JLabel) {
                JLabel label = (JLabel) c;
                if (value instanceof Artist) {
                    label.setText(((Artist) value).getName());
                    // Check how many albums of the artist are in the device
                    // If all of them are in the device, the text will be green
                    // If some of them are in the device, the text will be blue
                    // If none of them are in the device, the text will be white
                    int totalAlbums = 0, albumsInDevice = 0;
                    for (Album album: ((Artist) value).getAlbums()) {
                        totalAlbums++;
                        if (device.getAlbum(album.getArtist(), album.getTitle()) != null) {
                            albumsInDevice++;
                        }
                    }
                    if (albumsInDevice == totalAlbums) {
                        label.setForeground(Color.GREEN);
                    } else if (albumsInDevice > 0) {
                        label.setForeground(Color.CYAN);
                    } else {
                        label.setForeground(Color.WHITE);
                    }
                }
            }
            return c;
        }
    };

    ListCellRenderer<Object> albumListRenderer = new DefaultListCellRenderer() {
        @Override
        public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
            Component c = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
            if (c instanceof JLabel) {
                JLabel label = (JLabel) c;
                if (value instanceof Album) {
                    label.setText(((Album) value).getTitle());
                    // Check if the album is in the device
                    if (device.getAlbum(((Album) value).getArtist(), ((Album) value).getTitle()) != null) {
                        label.setForeground(Color.GREEN);
                    } else {
                        label.setForeground(Color.WHITE);
                    }
                }
            }
            return c;
        }
    };

    ListCellRenderer<Object> variantListRenderer = new DefaultListCellRenderer() {
        @Override
        public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
            Component c = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
            if (c instanceof JLabel) {
                JLabel label = (JLabel) c;
                if (value instanceof AlbumVariant) {
                    label.setText(albumVariantToString((AlbumVariant) value));
                    // Check if the variant is in the device
                    if (
                            device.getAlbumVariant(
                                    ((AlbumVariant) value).getArtist(),
                                    ((AlbumVariant) value).getTitle(),
                                    ((AlbumVariant) value).getFormat(),
                                    ((AlbumVariant) value).getBits(),
                                    ((AlbumVariant) value).getSample()
                            ) != null
                    ) {
                        label.setForeground(Color.GREEN);
                    } else {
                        label.setForeground(Color.WHITE);
                    }
                }
            }
            return c;
        }
    };

    private String albumVariantToString(AlbumVariant variant) {
        return variant.getFormat() + " / " + variant.getBits() + " - " + variant.getSample();
    }

    private Integer getAlbumVariantFormatFromString(String variantString) {
        return Integer.parseInt(variantString.split(" / ")[0]);
    }

    private Integer getAlbumVariantBitsFromString(String variantString) {
        return Integer.parseInt(variantString.split(" / ")[1].split(" - ")[0]);
    }

    private Integer getAlbumVariantSampleFromString(String variantString) {
        return Integer.parseInt(variantString.split(" / ")[1].split(" - ")[1]);
    }

    private void updateProgressStatus(String type, AlbumVariant variant, Color color) {
        progressActionType.setText(type);
        progressActionType.setForeground(color);
        progressActionArtist.setText(variant.getArtist());
        progressActionAlbum.setText(variant.getTitle());
        progressActionVariant.setText(albumVariantToString(variant));
        progressAction.setValue(0);
    }

    private void updateProgressMessage(String message, Color color) {
        progressActionResult.setText(message);
        progressActionResult.setForeground(color);
    }

    private void sendButton() {
        updateProgressMessage("Preparant l'enviament...", Color.CYAN);
        // Get the selected variant
        AlbumVariant variant = (AlbumVariant) listLibraryVariants.getSelectedValue();
        if (variant == null) {
            updateProgressMessage("No s'ha seleccionat cap variant", Color.RED);
            return;
        }

        updateProgressStatus("Enviant:", variant, Color.GREEN);

        // Check if any variant of the album is already in the device
        // If so, delete it before sending the new one
        Album album = library.getAlbum(variant.getArtist(), variant.getTitle());
        if (album == null) {
            updateProgressMessage("No s'ha trobat aquest àlbum", Color.RED);
            return;
        }
        updateProgressMessage("Netejant variants ja existents...", Color.YELLOW);
        for (AlbumVariant v: album.getVariants()) {
            if (device.getAlbumVariant(v.getArtist(), v.getTitle(), v.getFormat(), v.getBits(), v.getSample()) != null) {
                delete(v);
            }
        }

        // Send the variant
        updateProgressMessage("Enviant...", Color.YELLOW);
        send(variant);
    }

    private void deleteButton() {
        updateProgressMessage("Preparant l'esborrament...", Color.CYAN);
        // Get the selected variant
        AlbumVariant variant = (AlbumVariant) listLibraryVariants.getSelectedValue();
        if (variant == null) {
            updateProgressMessage("No s'ha seleccionat cap variant", Color.RED);
            return;
        }

        updateProgressStatus("Esborrant:", variant, Color.RED);

        // Delete the variant
        updateProgressMessage("Esborrant...", Color.YELLOW);
        delete(variant);
    }

    private void send(AlbumVariant variant) {
        SwingWorker<Void, Integer> worker = new SwingWorker<>() {
            @Override
            protected Void doInBackground() {
                // Get the artist
                Artist artist = library.getArtist(variant.getArtist());
                if (artist == null) {
                    publish(-10);
                    return null;
                }

                // Get the album
                Album album = artist.getAlbum(variant.getTitle());
                if (album == null) {
                    publish(-11);
                    return null;
                }

                // Get the variant
                AlbumVariant variantToSend = album.getVariant(variant.getFormat(), variant.getBits(), variant.getSample());
                if (variantToSend == null) {
                    publish(-12);
                    return null;
                }

                // Generate path
                String destPath = device.getPath() + "/" + artist.getName() + "/" + variantToSend.getDirectoryName();

                // Send the variant to the device
                boolean result = DirectoryManagement.copyDirectory(variant.getPath(), destPath, this::publish);
                if (result) {
                    publish(100);
                } else {
                    publish(-1);
                }
                return null;
            }

            @Override
            protected void process(List<Integer> chunks) {
                for (int progress : chunks) {
                    switch (progress) {
                        case -1 -> updateProgressMessage("Error en l'enviament", Color.RED);
                        case -10 -> updateProgressMessage("Error en la cerca de l'artista", Color.RED);
                        case -11 -> updateProgressMessage("Error en la cerca de l'àlbum", Color.RED);
                        case -12 -> updateProgressMessage("Error en la cerca de la variant", Color.RED);
                        default -> progressAction.setValue(progress);
                    }
                }
            }

            @Override
            protected void done() {
                updateSources();
                if (progressAction.getValue() == 100) {
                    updateProgressMessage("Enviament completat", Color.GREEN);
                }
            }
        };

        worker.execute();
    }

    private void delete(AlbumVariant variant) {
        SwingWorker<Void, Integer> worker = new SwingWorker<>() {
            @Override
            protected Void doInBackground() {
                // Get the artist
                Artist artist = library.getArtist(variant.getArtist());
                if (artist == null) {
                    publish(-10);
                    return null;
                }

                // Get the album
                Album album = artist.getAlbum(variant.getTitle());
                if (album == null) {
                    publish(-11);
                    return null;
                }

                // Get the variant
                AlbumVariant variantToDelete = album.getVariant(variant.getFormat(), variant.getBits(), variant.getSample());
                if (variantToDelete == null) {
                    publish(-12);
                    return null;
                }

                String destPath = device.getPath() + "/" + artist.getName() + "/" + variantToDelete.getDirectoryName();

                // Delete the variant from the device
                boolean result = DirectoryManagement.deleteDirectory(destPath, this::publish);

                // If the artist directory is empty, delete it
                /*if (result) {
                    String artistPath = device.getPath() + "/" + artist.getName();
                    if (DirectoryManagement.isDirectoryEmpty(artistPath)) {
                        DirectoryManagement.deleteDirectory(artistPath, this::publish);
                    }
                }*/

                if (result) {
                    publish(100);
                } else {
                    publish(-1);
                }
                return null;
            }

            @Override
            protected void process(List<Integer> chunks) {
                for (int progress : chunks) {
                    switch (progress) {
                        case -1 -> updateProgressMessage("Error en l'esborrament", Color.RED);
                        case -10 -> updateProgressMessage("Error en la cerca de l'artista", Color.RED);
                        case -11 -> updateProgressMessage("Error en la cerca de l'àlbum", Color.RED);
                        case -12 -> updateProgressMessage("Error en la cerca de la variant", Color.RED);
                        default -> progressAction.setValue(progress);
                    }
                }
            }

            @Override
            protected void done() {
                updateSources();
                if (progressAction.getValue() == 100) {
                    updateProgressMessage("Esborrament completat", Color.GREEN);
                }
            }
        };

        worker.execute();
    }

    private void updateSources() {
        // Get the current selected items
        int selectedArtist = listLibraryArtists.getSelectedIndex();
        int selectedAlbum = listLibraryAlbums.getSelectedIndex();
        int selectedVariant = listLibraryVariants.getSelectedIndex();

        library = new Library(library.getPath());
        device = new Library(device.getPath());
        updateLibraryArtists();
        //updateLibraryAlbumList();
        //updateLibraryVariants();

        listLibraryArtists.addListSelectionListener(e -> updateLibraryAlbumList());
        listLibraryAlbums.addListSelectionListener(e -> updateLibraryVariants());

        // Re-render the lists using the custom renderers
        listLibraryArtists.repaint();
        listLibraryAlbums.repaint();
        listLibraryVariants.repaint();

        // Re-select the items
        listLibraryArtists.setSelectedIndex(selectedArtist);
        listLibraryAlbums.setSelectedIndex(selectedAlbum);
        listLibraryVariants.setSelectedIndex(selectedVariant);
    }

    private void updateStatus() {
        //// LIBRARY ////
        if (libraryLastStatus) { // Was connected last time
            if (library.isConnected()) { // Still connected
                labelStatusLibrary.setForeground(Color.GREEN);
                labelStatusLibrary.setText("Llibreria: " + library.getPath());
            } else { // Disconnected
                libraryLastStatus = false;
                labelStatusLibrary.setForeground(Color.RED);
                labelStatusLibrary.setText("Llibreria: " + library.getPath() + " (s'ha desconnectat)");
            }
        } else { // Was disconnected last time
            if (library.isConnected()) { // Connected
                libraryLastStatus = true;
                labelStatusLibrary.setForeground(Color.GREEN);
                labelStatusLibrary.setText("Llibreria: " + library.getPath());
            } else { // Still disconnected
                labelStatusLibrary.setForeground(Color.RED);
                labelStatusLibrary.setText("Llibreria: " + library.getPath() + " (no connectada)");
            }
            updateSources();
        }

        //// DEVICE ////
        if (deviceLastStatus) { // Was connected last time
            if (device.isConnected()) { // Still connected
                labelStatusDevice.setForeground(Color.GREEN);
                labelStatusDevice.setText("Dispositiu: " + device.getPath());
            } else { // Disconnected
                deviceLastStatus = false;
                labelStatusDevice.setForeground(Color.RED);
                labelStatusDevice.setText("Dispositiu: " + device.getPath() + " (s'ha desconnectat)");
            }
        } else { // Was disconnected last time
            if (device.isConnected()) { // Connected
                deviceLastStatus = true;
                labelStatusDevice.setForeground(Color.GREEN);
                labelStatusDevice.setText("Dispositiu: " + device.getPath());
            } else { // Still disconnected
                labelStatusDevice.setForeground(Color.RED);
                labelStatusDevice.setText("Dispositiu: " + device.getPath() + " (no connectat)");
            }
            updateSources();
        }
    }

}
