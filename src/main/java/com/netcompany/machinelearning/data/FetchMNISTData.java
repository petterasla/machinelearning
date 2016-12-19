package com.netcompany.machinelearning.data;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import org.apache.commons.io.FileUtils;
import org.deeplearning4j.util.ArchiveUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FetchMNISTData {
    protected static final Logger log = LoggerFactory.getLogger(org.deeplearning4j.base.MnistFetcher.class);
    protected File BASE_DIR = new File(System.getProperty("user.dir"));
    protected static final String LOCAL_DIR_NAME = "mnist";
    protected File FILE_DIR;
    private File fileDir;
    private static final String trainingFilesURL = "http://yann.lecun.com/exdb/mnist/train-images-idx3-ubyte.gz";
    private static final String trainingFilesFilename = "images-idx3-ubyte.gz";
    public static final String trainingFilesFilename_unzipped = "images-idx3-ubyte";
    private static final String trainingFileLabelsURL = "http://yann.lecun.com/exdb/mnist/train-labels-idx1-ubyte.gz";
    private static final String trainingFileLabelsFilename = "labels-idx1-ubyte.gz";
    public static final String trainingFileLabelsFilename_unzipped = "labels-idx1-ubyte";
    private static final String testFilesURL = "http://yann.lecun.com/exdb/mnist/t10k-images-idx3-ubyte.gz";
    private static final String testFilesFilename = "t10k-images-idx3-ubyte.gz";
    public static final String testFilesFilename_unzipped = "t10k-images-idx3-ubyte";
    private static final String testFileLabelsURL = "http://yann.lecun.com/exdb/mnist/t10k-labels-idx1-ubyte.gz";
    private static final String testFileLabelsFilename = "t10k-labels-idx1-ubyte.gz";
    public static final String testFileLabelsFilename_unzipped = "t10k-labels-idx1-ubyte";

    public File downloadAndUntar() throws IOException {
        if(this.fileDir != null) {
            return this.fileDir;
        } else {
            File baseDir = this.FILE_DIR;
            if(!baseDir.isDirectory() && !baseDir.mkdir()) {
                throw new IOException("Could not mkdir " + baseDir);
            } else {
                log.info("Downloading mnist...");
                File tarFile = new File(baseDir, "images-idx3-ubyte.gz");
                File tarFileLabels = new File(baseDir, "t10k-images-idx3-ubyte.gz");
                if(!tarFile.isFile()) {
                    FileUtils.copyURLToFile(new URL("http://yann.lecun.com/exdb/mnist/train-images-idx3-ubyte.gz"), tarFile);
                }

                if(!tarFileLabels.isFile()) {
                    FileUtils.copyURLToFile(new URL("http://yann.lecun.com/exdb/mnist/t10k-images-idx3-ubyte.gz"), tarFileLabels);
                }

                ArchiveUtils.unzipFileTo(tarFile.getAbsolutePath(), baseDir.getAbsolutePath());
                ArchiveUtils.unzipFileTo(tarFileLabels.getAbsolutePath(), baseDir.getAbsolutePath());
                File labels = new File(baseDir, "labels-idx1-ubyte.gz");
                File labelsTest = new File(baseDir, "t10k-labels-idx1-ubyte.gz");
                if(!labels.isFile()) {
                    FileUtils.copyURLToFile(new URL("http://yann.lecun.com/exdb/mnist/train-labels-idx1-ubyte.gz"), labels);
                }

                if(!labelsTest.isFile()) {
                    FileUtils.copyURLToFile(new URL("http://yann.lecun.com/exdb/mnist/t10k-labels-idx1-ubyte.gz"), labelsTest);
                }

                ArchiveUtils.unzipFileTo(labels.getAbsolutePath(), baseDir.getAbsolutePath());
                ArchiveUtils.unzipFileTo(labelsTest.getAbsolutePath(), baseDir.getAbsolutePath());
                this.fileDir = baseDir;
                return this.fileDir;
            }
        }
    }

    public static void gunzipFile(File baseDir, File gzFile) throws IOException {
        log.info("gunzip\'ing File: " + gzFile.toString());
        Process p = Runtime.getRuntime().exec(String.format("gunzip %s", new Object[]{gzFile.getAbsolutePath()}));
        BufferedReader stdError = new BufferedReader(new InputStreamReader(p.getErrorStream()));
        log.info("Here is the standard error of the command (if any):\n");

        String s;
        while((s = stdError.readLine()) != null) {
            log.info(s);
        }

        stdError.close();
    }

    public File getBASE_DIR() {
        return this.BASE_DIR;
    }

    public File getFILE_DIR() {
        return this.FILE_DIR;
    }

    public File getFileDir() {
        return this.fileDir;
    }

    public void setBASE_DIR(File BASE_DIR) {
        this.BASE_DIR = BASE_DIR;
    }

    public void setFILE_DIR(File FILE_DIR) {
        this.FILE_DIR = FILE_DIR;
    }

    public void setFileDir(File fileDir) {
        this.fileDir = fileDir;
    }

    public boolean equals(Object o) {
        if(o == this) {
            return true;
        } else if(!(o instanceof FetchMNISTData)) {
            return false;
        } else {
            FetchMNISTData other = (FetchMNISTData)o;
            if(!other.canEqual(this)) {
                return false;
            } else {
                label47: {
                    File this$BASE_DIR = this.getBASE_DIR();
                    File other$BASE_DIR = other.getBASE_DIR();
                    if(this$BASE_DIR == null) {
                        if(other$BASE_DIR == null) {
                            break label47;
                        }
                    } else if(this$BASE_DIR.equals(other$BASE_DIR)) {
                        break label47;
                    }

                    return false;
                }

                File this$FILE_DIR = this.getFILE_DIR();
                File other$FILE_DIR = other.getFILE_DIR();
                if(this$FILE_DIR == null) {
                    if(other$FILE_DIR != null) {
                        return false;
                    }
                } else if(!this$FILE_DIR.equals(other$FILE_DIR)) {
                    return false;
                }

                File this$fileDir = this.getFileDir();
                File other$fileDir = other.getFileDir();
                if(this$fileDir == null) {
                    if(other$fileDir != null) {
                        return false;
                    }
                } else if(!this$fileDir.equals(other$fileDir)) {
                    return false;
                }

                return true;
            }
        }
    }

    protected boolean canEqual(Object other) {
        return other instanceof org.deeplearning4j.base.MnistFetcher;
    }

    public int hashCode() {
        boolean PRIME = true;
        byte result = 1;
        File $BASE_DIR = this.getBASE_DIR();
        int result1 = result * 59 + ($BASE_DIR == null?0:$BASE_DIR.hashCode());
        File $FILE_DIR = this.getFILE_DIR();
        result1 = result1 * 59 + ($FILE_DIR == null?0:$FILE_DIR.hashCode());
        File $fileDir = this.getFileDir();
        result1 = result1 * 59 + ($fileDir == null?0:$fileDir.hashCode());
        return result1;
    }

    public String toString() {
        return "MnistFetcher(BASE_DIR=" + this.getBASE_DIR() + ", FILE_DIR=" + this.getFILE_DIR() + ", fileDir=" + this.getFileDir() + ")";
    }

    public FetchMNISTData() {
        this.FILE_DIR = new File(this.BASE_DIR + "/src/main/java/com/netcompany/machinelearning/data", "MNIST");
    }
}
