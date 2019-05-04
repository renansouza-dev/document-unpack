package dev.renansouza.document;

import io.reactivex.Single;

import javax.validation.ValidationException;

class DocumentValidation {

    static Single<Document> validateDocumentExtension(Document document) {
        return Single.defer(() -> Single.just(document))
                .filter(DocumentValidation::filenameValidation)
                .switchIfEmpty(Single.error(new ValidationException("Filename is empty")))
                .filter(DocumentValidation::extensionValidation)
                .switchIfEmpty(Single.error(new ValidationException("Extension not supported")));
    }

    private static boolean filenameValidation(Document doc) {
        return doc.getFilename() != null && !doc.getFilename().equals("");
    }

    private static boolean extensionValidation(Document doc) {
        return doc.getExtension().equals(".zip") || doc.getExtension().equals(".7z") || doc.getExtension().equals(".xml");
    }
}
