import pl.edu.icm.cermine.ContentExtractor;
public class ReferenceExtractor {

    public static void main(String[] args) {

        try {
            ContentExtractor extractor = new ContentExtractor();
            //TODO: Load multiple files from a folder
            String file = "/home/nick/PycharmProjects/reference_extracter/test_article.pdf";
            Article article = new Article(file, extractor);

            System.out.println(article);
            for(Article reference : article.getReferences()) {
                System.out.println(reference);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
