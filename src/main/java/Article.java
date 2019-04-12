import pl.edu.icm.cermine.ContentExtractor;
import pl.edu.icm.cermine.bibref.model.BibEntry;
import pl.edu.icm.cermine.bibref.model.BibEntryFieldType;
import pl.edu.icm.cermine.metadata.model.DateType;
import pl.edu.icm.cermine.metadata.model.DocumentAuthor;
import pl.edu.icm.cermine.metadata.model.DocumentMetadata;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class Article {

    private String title;
    private List<String> authors;
    private String date;
    private List<Article> references;

    public Article(String file, ContentExtractor contentExtractor) {
        try {
            InputStream inputStream = new FileInputStream(file);
            contentExtractor.setPDF(inputStream);
            // extract metadata
            DocumentMetadata documentMetaData = contentExtractor.getMetadata();
            // extract title
            this.title = documentMetaData.getTitle();
            // extract year
            this.date = documentMetaData.getDate(DateType.PUBLISHED).getYear();
            //extract authors
            List<DocumentAuthor> documentAuthors = documentMetaData.getAuthors();
            this.authors = new ArrayList<String>();
            for (DocumentAuthor documentAuthor : documentAuthors) {
                String author = documentAuthor.getName();
                this.authors.add(author);
            }
            // extract references
            List<BibEntry> result = contentExtractor.getReferences();
            this.references = new ArrayList<Article>();
            for (BibEntry bibEntry : result) {
                Article article = new Article(bibEntry);
                references.add(article);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Article(BibEntry bibEntry) {
        this.title = bibEntry.getFirstFieldValue(BibEntryFieldType.TITLE);
        this.authors = bibEntry.getAllFieldValues(BibEntryFieldType.AUTHOR);
        this.date = bibEntry.getFirstFieldValue(BibEntryFieldType.YEAR);
        this.references = new ArrayList<Article>();
    }

    public String getAuthorsString() {
        StringBuilder authorsStringBuilder = new StringBuilder();
        for(int i = 0; i < authors.size(); i++) {
            String author = authors.get(i);
            authorsStringBuilder.append(author);
            if(i < authors.size() - 1) {
                authorsStringBuilder.append("; ");
            }
        }
        return authorsStringBuilder.toString();
    }

    public String getReferencesString() {
        StringBuilder referencesStringBuilder = new StringBuilder();
        referencesStringBuilder.append("[\n");
        for(int i = 0; i < references.size(); i++) {
            String reference = references.get(i).fancyPrint();
            referencesStringBuilder.append(reference);
            if(i < references.size() - 1) {
                referencesStringBuilder.append(",\n");
            }
        }
        referencesStringBuilder.append("\n]");
        return referencesStringBuilder.toString();
    }

    public String getTitle() {
        return title;
    }

    public List<String> getAuthors() {
        return authors;
    }

    public String getDate() {
        return date;
    }

    public List<Article> getReferences() {
        return references;
    }

    @Override
    public String toString() {
        return String.format("%s\t%s\t%s\n", title, date, getAuthorsString());
    }

    public String fancyPrint() {
        return String.format("{\n" +
                "TITLE => \t %s\n" +
                "AUTHORS => \t %s\n" +
                "DATE => \t %s\n" +
                "}", this.title, this.getAuthorsString(), this.date
        );
    }

}
