package com.morocco.documentsapi.exception;

public enum ErrorCode {

    DOC_000("Erreur interne du serveur", "خطأ داخلي في الخادم"),
    DOC_001("Document introuvable", "الوثيقة غير موجودة"),
    DOC_002("Langue non supportée. Utilisez 'fr' ou 'ar'", "اللغة غير مدعومة. استعمل 'fr' أو 'ar'"),
    DOC_003("Le paramètre de recherche 'query' est requis", "معطى البحث 'query' مطلوب"),
    DOC_004("Requête invalide", "طلب غير صالح");

    private final String messageFr;
    private final String messageAr;

    ErrorCode(String messageFr, String messageAr) {
        this.messageFr = messageFr;
        this.messageAr = messageAr;
    }

    public String getMessage(String lang) {
        return "ar".equals(lang) ? messageAr : messageFr;
    }
}
