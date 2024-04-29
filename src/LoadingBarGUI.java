import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

public class LoadingBarGUI extends JFrame {
    //folder where the files will be deleted from
    public static final String FOLDER_PATH = "files";

    public LoadingBarGUI() {
        super("Delete Files");

        setSize(563, 392);

        setDefaultCloseOperation(EXIT_ON_CLOSE);

        setLocationRelativeTo(null);

        addGuiComponents();
    }

    private void addGuiComponents() {

        JButton deleteButton = new JButton("Delete Files");

        deleteButton.setFont(new Font("Dialog", Font.BOLD, 48));

        //action of the button when clicked on
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                File folder = new File(FOLDER_PATH);

                if (folder.isDirectory()){
                    File[] files = folder.listFiles();

                    if(files.length > 0){
                        deleteFiles(files);
                    }else{
                        showResultDialog("Folder Empty");
                    }
                }
            }
        });

        add(deleteButton, BorderLayout.CENTER);
    }

    private void showResultDialog(String message) {
        JDialog resultDialog = new JDialog(this, "Results", true);
        resultDialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        resultDialog.setSize(300, 150);
        resultDialog.setLocationRelativeTo(this);

        //message log
        JLabel messageLabel = new JLabel(message);
        messageLabel.setFont(new Font("Dialog", Font.BOLD, 26));
        messageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        resultDialog.add(messageLabel, BorderLayout.CENTER);

        //confirmation button
        JButton confirmButton = new JButton("Confirm");
        confirmButton.setFont(new Font("Dialog", Font.BOLD, 18));
        confirmButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                resultDialog.dispose();
            }
        });

        resultDialog.add(confirmButton, BorderLayout.SOUTH);
        resultDialog.setVisible(true);
    }

    private void deleteFiles(File[] files){
        JDialog loadingDialog = new JDialog(this, "Deleting Files", true);
        loadingDialog.setSize(300, 100);
        loadingDialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        loadingDialog.setLocationRelativeTo(this);

        //progress Bar
        JProgressBar progressBar = new JProgressBar();
        progressBar.setFont(new Font("Dialog", Font.BOLD, 18));

        //Blue Progress bar
        progressBar.setForeground(Color.BLUE);

        progressBar.setValue(0);

        //delete file thread
        Thread deleteFilesThread = new Thread(new Runnable() {
            @Override
            public void run() {
                //calculates the files in the 'files' folder
                int totalFiles = files.length;

                //amount of files deleted
                int filesDeleted = 0;

                //keeps track of progress
                int progress;

                for(File file : files){
                    //delete files
                    if(file.delete()){
                        filesDeleted++;

                        //progress percent
                         progress = (int) ((((double) filesDeleted / totalFiles) * 100));

                         try{
                             Thread.sleep(60);
                         }catch (InterruptedException interruptedException){
                             interruptedException.printStackTrace();
                         }

                         //update progress bar
                        progressBar.setValue(progress);
                    }

                }

                //remove loading dialog when done
                if(loadingDialog.isVisible()){
                    loadingDialog.dispose();
                }

                //shows results
                showResultDialog("Files Deleted");
            }
        });

        //starts the delete files thread
        deleteFilesThread.start();

        //add percent symbol
        progressBar.setStringPainted(true);

        loadingDialog.add(progressBar, BorderLayout.CENTER);
        loadingDialog.setVisible(true);

    }
}

