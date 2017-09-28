package com.brandwatch;

import org.apache.commons.io.FileUtils;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.common.SolrInputDocument;

import java.io.File;

@SuppressWarnings("deprecation")
public class App {


    public static void main(String[] args) throws Exception {

        String serverUrl = args[0];
        String pathName = args[1];

        SolrClient client = new HttpSolrClient(serverUrl);

        int count = 0;

        File[] files = new File(pathName).listFiles();

        String u = "https://github.com/BrandwatchLtd/";

        for (File f : files) {

            String name = f.getName().substring(0, f.getName().lastIndexOf("."));

            if(f.isDirectory()) {
                File[] moreFiles = f.listFiles();

                for(File file : moreFiles) {
                    if (!file.isDirectory()) {
                        String lastName = file.getName();
                        String last = lastName.substring(0, lastName.lastIndexOf("."));

                        SolrInputDocument doc = new SolrInputDocument();
                        doc.addField("url", "" + u + name + "/wiki/" + last);
                        doc.addField("body", FileUtils.readFileToString(file, "UTF-8"));

                        client.add(doc);

                        count++;
                        if (++count % 100 == 0) {
                            client.commit();
                        }
                    }
                }
            }
            client.commit();
        }
    }
}