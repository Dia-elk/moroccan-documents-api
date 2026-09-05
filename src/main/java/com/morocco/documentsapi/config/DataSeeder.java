package com.morocco.documentsapi.config;

import com.morocco.documentsapi.enums.DocumentCategoryEnum;
import com.morocco.documentsapi.model.Document;
import com.morocco.documentsapi.model.DocumentLocation;
import com.morocco.documentsapi.model.DocumentProcedure;
import com.morocco.documentsapi.model.DocumentRequirement;
import com.morocco.documentsapi.repository.DocumentLocationRepository;
import com.morocco.documentsapi.repository.DocumentProcedureRepository;
import com.morocco.documentsapi.repository.DocumentRepository;
import com.morocco.documentsapi.repository.DocumentRequirementRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.math.BigDecimal;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class DataSeeder {

    private final DocumentRepository documentRepository;
    private final DocumentRequirementRepository requirementRepository;
    private final DocumentProcedureRepository procedureRepository;
    private final DocumentLocationRepository locationRepository;

    private static final String PLACEHOLDER_PHONE = "+212 5XX-XXXXXX";
    private static final String STANDARD_HOURS = "Lundi - Vendredi : 08h30 - 16h30";

    @Bean
    ApplicationRunner seedDocuments() {
        return args -> {
            if (documentRepository.count() > 0) {
                log.info("Documents table already populated ({} rows) — skipping seed.", documentRepository.count());
                return;
            }

            log.info("Seeding reference data for 10 Moroccan administrative documents...");
            seedNationalId();
            seedPassport();
            seedDrivingLicense();
            seedMarriageCertificate();
            seedBirthCertificate();
            seedBusinessLicense();
            seedCommercialRegister();
            seedVisaSchengen();
            seedLandTitle();
            seedProofOfResidence();
            log.info("Seed complete: {} documents created.", documentRepository.count());
        };
    }

    private void seedNationalId() {
        Document doc = documentRepository.save(Document.builder()
                .code("national-id")
                .nameFr("Carte Nationale d'Identité Électronique")
                .nameAr("البطاقة الوطنية للتعريف الإلكترونية")
                .descriptionFr("Document officiel obligatoire attestant de l'identité et de la nationalité marocaine, requis pour toute démarche administrative.")
                .descriptionAr("وثيقة رسمية إلزامية تثبت الهوية والجنسية المغربية، وتلزم لإنجاز جميع الإجراءات الإدارية.")
                .category(DocumentCategoryEnum.IDENTITY)
                .feeMad(new BigDecimal("100.00"))
                .processingDays(15)
                .locationFr("Préfecture ou Province (Wilaya) du lieu de résidence")
                .locationAr("عمالة أو إقليم (الولاية) بمكان الإقامة")
                .build());

        saveRequirements(doc,
                text("Copie intégrale de l'acte de naissance datant de moins de 3 mois", "نسخة كاملة من عقد الازدياد لا يتجاوز تاريخها 3 أشهر"),
                text("Certificat de résidence ou justificatif de domicile", "شهادة السكنى أو ما يثبت العنوان"),
                text("Deux photos d'identité récentes, format 4x6cm, fond blanc", "صورتان شمسيتان حديثتان، مقاس 4x6 سم، بخلفية بيضاء"),
                text("Ancienne carte d'identité nationale en cas de renouvellement", "البطاقة الوطنية القديمة في حالة التجديد")
        );

        saveProcedures(doc,
                text("Se présenter en personne au guichet de la carte d'identité de la préfecture ou province", "التوجه شخصيًا إلى شباك البطاقة الوطنية بالعمالة أو الإقليم"),
                text("Retirer et remplir le formulaire de demande", "سحب وملء استمارة الطلب"),
                text("Déposer le dossier complet avec les originaux et copies", "إيداع الملف كاملاً مع الوثائق الأصلية ونسخها"),
                text("Acquitter les frais de timbre fiscal de 100 MAD", "أداء واجبات الطابع الجبائي البالغة 100 درهم"),
                text("Retirer la carte à la date indiquée sur le récépissé", "استخراج البطاقة في التاريخ المحدد في الوصل")
        );

        saveLocation(doc,
                "Bureau de la Carte Nationale d'Identité — Préfecture", "مكتب البطاقة الوطنية للتعريف - العمالة",
                "Siège de la Préfecture ou Province de votre lieu de résidence", "مقر العمالة أو الإقليم بمكان إقامتكم",
                PLACEHOLDER_PHONE, "contact@interieur.gov.ma", STANDARD_HOURS);
    }

    private void seedPassport() {
        Document doc = documentRepository.save(Document.builder()
                .code("passport")
                .nameFr("Passeport Biométrique Marocain")
                .nameAr("جواز السفر المغربي البيومتري")
                .descriptionFr("Document de voyage international biométrique permettant de voyager à l'étranger et de justifier de l'identité et de la nationalité marocaine.")
                .descriptionAr("وثيقة سفر دولية بيومترية تخول التنقل خارج الوطن وتثبت الهوية والجنسية المغربية.")
                .category(DocumentCategoryEnum.TRAVEL)
                .feeMad(new BigDecimal("400.00"))
                .processingDays(10)
                .locationFr("Agence Nationale des Titres Sécurisés (ANTS)")
                .locationAr("الوكالة الوطنية للسجلات المؤمنة")
                .build());

        saveRequirements(doc,
                text("Copie de la Carte Nationale d'Identité Électronique", "نسخة من البطاقة الوطنية للتعريف الإلكترونية"),
                text("Copie certifiée conforme de l'acte de naissance", "نسخة من عقد الازدياد مطابقة للأصل"),
                text("Quatre photos d'identité récentes aux normes biométriques", "أربع صور شمسية حديثة وفق المعايير البيومترية"),
                text("Formulaire de demande de passeport dûment rempli", "استمارة طلب جواز السفر معبأة بشكل كامل"),
                text("Ancien passeport en cas de renouvellement", "جواز السفر القديم في حالة التجديد")
        );

        saveProcedures(doc,
                text("Prendre rendez-vous en ligne ou se présenter au guichet ANTS", "أخذ موعد عبر الإنترنت أو التوجه إلى شباك الوكالة"),
                text("Déposer le dossier complet et procéder à la capture biométrique (photo et empreintes)", "إيداع الملف الكامل وإجراء المعالجة البيومترية (الصورة والبصمات)"),
                text("Payer les frais de timbre fiscal de 400 MAD", "أداء واجبات الطابع الجبائي البالغة 400 درهم"),
                text("Suivre l'état de la demande via le numéro de récépissé", "تتبع حالة الطلب برقم الوصل"),
                text("Récupérer le passeport au guichet une fois prêt", "استلام جواز السفر من الشباك عند جاهزيته")
        );

        saveLocation(doc,
                "Agence Nationale des Titres Sécurisés", "الوكالة الوطنية للسجلات المؤمنة",
                "Centre ANTS de votre préfecture ou province", "مركز الوكالة الوطنية للسجلات المؤمنة بعمالتكم أو إقليمكم",
                PLACEHOLDER_PHONE, "contact@ants.gov.ma", STANDARD_HOURS);
    }

    private void seedDrivingLicense() {
        Document doc = documentRepository.save(Document.builder()
                .code("driving-license")
                .nameFr("Permis de Conduire")
                .nameAr("رخصة السياقة")
                .descriptionFr("Autorisation officielle permettant de conduire un véhicule à moteur sur le territoire marocain, délivrée après réussite aux examens théorique et pratique.")
                .descriptionAr("ترخيص رسمي يخول قيادة عربة ذات محرك داخل التراب الوطني، يُسلَّم بعد اجتياز الاختبارين النظري والتطبيقي.")
                .category(DocumentCategoryEnum.TRANSPORT)
                .feeMad(new BigDecimal("200.00"))
                .processingDays(5)
                .locationFr("Bureau des Permis de Conduire (Ministère de l'Équipement et de l'Eau)")
                .locationAr("مكتب رخص السياقة (وزارة التجهيز والماء)")
                .build());

        saveRequirements(doc,
                text("Copie de la Carte Nationale d'Identité Électronique", "نسخة من البطاقة الوطنية للتعريف الإلكترونية"),
                text("Certificat médical d'aptitude à la conduite", "شهادة طبية تثبت القدرة على السياقة"),
                text("Photo d'identité récente", "صورة شمسية حديثة"),
                text("Justificatif de domicile", "شهادة السكنى"),
                text("Attestation de fin de formation d'une auto-école agréée", "شهادة نهاية التكوين من مدرسة تعليم السياقة المعتمدة")
        );

        saveProcedures(doc,
                text("S'inscrire dans une auto-école agréée et suivre la formation", "التسجيل في مدرسة معتمدة لتعليم السياقة ومتابعة التكوين"),
                text("Passer et réussir l'examen théorique (code de la route)", "اجتياز الاختبار النظري (قانون السير) بنجاح"),
                text("Passer et réussir l'examen pratique de conduite", "اجتياز اختبار السياقة التطبيقي بنجاح"),
                text("Déposer le dossier complet au bureau des permis de conduire", "إيداع الملف الكامل بمكتب رخص السياقة"),
                text("Payer les frais de 200 MAD et retirer le permis", "أداء رسوم 200 درهم واستخراج الرخصة")
        );

        saveLocation(doc,
                "Bureau des Permis de Conduire", "مكتب رخص السياقة",
                "Direction Provinciale de l'Équipement et de l'Eau de votre préfecture", "المديرية الإقليمية للتجهيز والماء بعمالتكم",
                PLACEHOLDER_PHONE, "contact@equipement.gov.ma", STANDARD_HOURS);
    }

    private void seedMarriageCertificate() {
        Document doc = documentRepository.save(Document.builder()
                .code("marriage-certificate")
                .nameFr("Acte de Mariage")
                .nameAr("عقد الزواج")
                .descriptionFr("Document officiel attestant de l'union matrimoniale, délivré par la commune où le mariage a été célébré et enregistré.")
                .descriptionAr("وثيقة رسمية تثبت الرابطة الزوجية، تسلمها الجماعة التي تم فيها إبرام الزواج وتسجيله.")
                .category(DocumentCategoryEnum.CIVIL_STATUS)
                .feeMad(new BigDecimal("50.00"))
                .processingDays(3)
                .locationFr("Commune (Bureau de l'État Civil) du lieu de célébration du mariage")
                .locationAr("الجماعة (مكتب الحالة المدنية) بمكان إبرام الزواج")
                .build());

        saveRequirements(doc,
                text("Copie de la Carte Nationale d'Identité Électronique des deux époux", "نسخة من البطاقة الوطنية للتعريف لكلا الزوجين"),
                text("Copie de l'acte de naissance de chaque époux", "نسخة من عقد ازدياد كل واحد من الزوجين"),
                text("Livret de famille (le cas échéant)", "دفتر الحالة المدنية العائلي (إن وجد)")
        );

        saveProcedures(doc,
                text("Se présenter au bureau de l'état civil de la commune où le mariage a été célébré", "التوجه إلى مكتب الحالة المدنية بالجماعة التي أُبرم فيها الزواج"),
                text("Présenter les pièces justificatives et remplir la demande", "تقديم الوثائق المثبتة وملء الطلب"),
                text("Payer les frais de 50 MAD", "أداء رسوم قدرها 50 درهم"),
                text("Récupérer l'acte de mariage délivré", "استلام عقد الزواج المسلم")
        );

        saveLocation(doc,
                "Bureau de l'État Civil — Commune", "مكتب الحالة المدنية - الجماعة",
                "Siège de la Commune où le mariage a été célébré", "مقر الجماعة التي تم فيها إبرام الزواج",
                PLACEHOLDER_PHONE, "contact@commune.gov.ma", STANDARD_HOURS);
    }

    private void seedBirthCertificate() {
        Document doc = documentRepository.save(Document.builder()
                .code("birth-certificate")
                .nameFr("Extrait d'Acte de Naissance")
                .nameAr("مستخرج من رسم الولادة")
                .descriptionFr("Copie ou extrait officiel de l'acte de naissance, indispensable pour la quasi-totalité des démarches administratives.")
                .descriptionAr("نسخة أو مستخرج رسمي من رسم الولادة، ضروري لإنجاز جل الإجراءات الإدارية.")
                .category(DocumentCategoryEnum.CIVIL_STATUS)
                .feeMad(new BigDecimal("30.00"))
                .processingDays(1)
                .locationFr("Commune (Bureau de l'État Civil) du lieu de naissance")
                .locationAr("الجماعة (مكتب الحالة المدنية) بمكان الازدياد")
                .build());

        saveRequirements(doc,
                text("Carte Nationale d'Identité Électronique ou justificatif de lien de parenté", "البطاقة الوطنية للتعريف أو ما يثبت صلة القرابة"),
                text("Numéro d'acte de naissance ou livret de famille (si disponible)", "رقم رسم الولادة أو دفتر الحالة المدنية (إن توفر)")
        );

        saveProcedures(doc,
                text("Se présenter au bureau d'état civil de la commune de naissance ou utiliser le portail national des services publics", "التوجه إلى مكتب الحالة المدنية بجماعة الازدياد أو استعمال البوابة الوطنية للخدمات العمومية"),
                text("Indiquer le numéro d'acte et l'identité du demandeur", "الإدلاء برقم الرسم وهوية الطالب"),
                text("Payer les frais de 30 MAD", "أداء رسوم قدرها 30 درهم"),
                text("Récupérer l'extrait immédiatement ou par voie électronique", "استلام المستخرج فورًا أو عبر الوسائل الإلكترونية")
        );

        saveLocation(doc,
                "Bureau de l'État Civil — Commune", "مكتب الحالة المدنية - الجماعة",
                "Siège de la Commune du lieu de naissance", "مقر الجماعة بمكان الازدياد",
                PLACEHOLDER_PHONE, "contact@commune.gov.ma", STANDARD_HOURS);
    }

    private void seedBusinessLicense() {
        Document doc = documentRepository.save(Document.builder()
                .code("business-license")
                .nameFr("Patente Commerciale")
                .nameAr("الباتنتة (الرخصة التجارية)")
                .descriptionFr("Autorisation administrative et fiscale obligatoire pour exercer une activité commerciale, industrielle ou artisanale.")
                .descriptionAr("ترخيص إداري وضريبي إلزامي لممارسة نشاط تجاري أو صناعي أو حرفي.")
                .category(DocumentCategoryEnum.COMMERCIAL)
                .feeMad(new BigDecimal("500.00"))
                .processingDays(5)
                .locationFr("Commune + Centre Régional d'Investissement (Guichet Unique)")
                .locationAr("الجماعة + المركز الجهوي للاستثمار (الشباك الوحيد)")
                .build());

        saveRequirements(doc,
                text("Copie de la Carte Nationale d'Identité Électronique du demandeur", "نسخة من البطاقة الوطنية للتعريف الخاصة بالطالب"),
                text("Justificatif de propriété ou contrat de bail du local commercial", "ما يثبت ملكية أو كراء المحل التجاري"),
                text("Certificat d'immatriculation fiscale (identifiant fiscal)", "شهادة التسجيل الضريبي (المعرف الضريبي)"),
                text("Plan de situation du local d'exploitation", "تصميم لموقع المحل المستغل")
        );

        saveProcedures(doc,
                text("Déposer le dossier auprès du Centre Régional d'Investissement ou de la commune", "إيداع الملف لدى المركز الجهوي للاستثمار أو الجماعة"),
                text("Faire constater et viser le local par les services compétents", "معاينة المحل والتأشير عليه من طرف المصالح المختصة"),
                text("Payer les frais d'enregistrement de 500 MAD", "أداء رسوم التسجيل البالغة 500 درهم"),
                text("Retirer l'attestation de patente délivrée", "استخراج شهادة الباتنتة المسلمة")
        );

        saveLocation(doc,
                "Centre Régional d'Investissement", "المركز الجهوي للاستثمار",
                "Guichet Unique du Centre Régional d'Investissement de votre région", "الشباك الوحيد للمركز الجهوي للاستثمار بجهتكم",
                PLACEHOLDER_PHONE, "contact@cri.gov.ma", STANDARD_HOURS);
    }

    private void seedCommercialRegister() {
        Document doc = documentRepository.save(Document.builder()
                .code("commercial-register")
                .nameFr("Registre de Commerce")
                .nameAr("السجل التجاري")
                .descriptionFr("Inscription légale obligatoire de toute entreprise ou commerçant, tenue par le tribunal de commerce compétent.")
                .descriptionAr("تقييد قانوني إلزامي لكل مقاولة أو تاجر، تتكفل به المحكمة التجارية المختصة.")
                .category(DocumentCategoryEnum.COMMERCIAL)
                .feeMad(new BigDecimal("300.00"))
                .processingDays(7)
                .locationFr("Tribunal de Commerce (Secrétariat-Greffe)")
                .locationAr("المحكمة التجارية (كتابة الضبط)")
                .build());

        saveRequirements(doc,
                text("Patente commerciale ou avis d'imposition", "الباتنتة التجارية أو الإشعار الضريبي"),
                text("Copie de la Carte Nationale d'Identité Électronique du gérant", "نسخة من البطاقة الوطنية للتعريف الخاصة بالمسير"),
                text("Statuts de la société ou plan d'affaires pour les personnes morales", "النظام الأساسي للشركة أو مخطط العمل بالنسبة للأشخاص المعنويين"),
                text("Certificat négatif (attestation de dénomination)", "الشهادة السلبية (شهادة التسمية)")
        );

        saveProcedures(doc,
                text("Obtenir le certificat négatif auprès de l'OMPIC", "الحصول على الشهادة السلبية من المكتب المغربي للملكية الصناعية والتجارية"),
                text("Déposer le dossier d'immatriculation au secrétariat-greffe du tribunal de commerce", "إيداع ملف التقييد بكتابة ضبط المحكمة التجارية"),
                text("Payer les frais de greffe de 300 MAD", "أداء رسوم كتابة الضبط البالغة 300 درهم"),
                text("Retirer l'extrait du registre de commerce (modèle 7)", "استخراج مستخرج السجل التجاري (النموذج 7)")
        );

        saveLocation(doc,
                "Secrétariat-Greffe du Tribunal de Commerce", "كتابة الضبط بالمحكمة التجارية",
                "Siège du Tribunal de Commerce de votre ville", "مقر المحكمة التجارية بمدينتكم",
                PLACEHOLDER_PHONE, "contact@justice.gov.ma", STANDARD_HOURS);
    }

    private void seedVisaSchengen() {
        Document doc = documentRepository.save(Document.builder()
                .code("visa-schengen")
                .nameFr("Visa Schengen")
                .nameAr("تأشيرة شنغن")
                .descriptionFr("Autorisation d'entrée de court séjour permettant de circuler dans l'espace Schengen, délivrée par l'ambassade ou le consulat du pays de destination principale.")
                .descriptionAr("ترخيص دخول لإقامة قصيرة يخول التنقل داخل فضاء شنغن، تسلمه سفارة أو قنصلية بلد الوجهة الرئيسية.")
                .category(DocumentCategoryEnum.TRAVEL)
                .feeMad(new BigDecimal("80.00"))
                .feeCurrency("EUR")
                .processingDays(15)
                .locationFr("Ambassade ou Consulat du pays de destination")
                .locationAr("سفارة أو قنصلية بلد الوجهة")
                .feeNoteFr("Montant fixé en euros par les autorités consulaires ; peut être réglé en dirhams selon le taux de change en vigueur le jour du dépôt.")
                .feeNoteAr("مبلغ محدد باليورو من طرف السلطات القنصلية؛ يمكن أداؤه بالدرهم حسب سعر الصرف المعمول به يوم الإيداع.")
                .build());

        saveRequirements(doc,
                text("Passeport valide au moins 3 mois après la date de retour prévue", "جواز سفر ساري المفعول لمدة 3 أشهر على الأقل بعد تاريخ العودة المرتقب"),
                text("Réservation de vol aller-retour", "حجز تذكرة السفر ذهابًا وإيابًا"),
                text("Justificatif d'hébergement (réservation d'hôtel ou attestation d'accueil)", "ما يثبت الإيواء (حجز فندقي أو شهادة استقبال)"),
                text("Preuve de moyens financiers suffisants (relevés bancaires)", "إثبات توفر موارد مالية كافية (كشوف بنكية)"),
                text("Assurance voyage couvrant l'espace Schengen", "تأمين السفر المغطي لفضاء شنغن")
        );

        saveProcedures(doc,
                text("Prendre rendez-vous auprès de l'ambassade, du consulat ou du centre de visas agréé", "أخذ موعد لدى السفارة أو القنصلية أو مركز التأشيرات المعتمد"),
                text("Constituer et déposer le dossier complet", "تكوين وإيداع الملف الكامل"),
                text("Payer les frais de visa de 80 EUR", "أداء رسوم التأشيرة البالغة 80 أورو"),
                text("Se soumettre à la prise d'empreintes biométriques", "الخضوع لأخذ البصمات البيومترية"),
                text("Suivre l'instruction du dossier et récupérer le passeport avec la décision", "تتبع معالجة الملف واسترجاع جواز السفر مرفوقًا بالقرار")
        );

        saveLocation(doc,
                "Ambassade / Consulat du pays de destination (exemple)", "سفارة / قنصلية بلد الوجهة (مثال)",
                "Voir le site officiel de l'ambassade ou du consulat concerné pour l'adresse exacte", "يرجى مراجعة الموقع الرسمي للسفارة أو القنصلية المعنية للحصول على العنوان الدقيق",
                PLACEHOLDER_PHONE, "contact@embassy-example.ma", "Lundi - Vendredi : 09h00 - 13h00 (sur rendez-vous)");
    }

    private void seedLandTitle() {
        Document doc = documentRepository.save(Document.builder()
                .code("land-title")
                .nameFr("Titre Foncier")
                .nameAr("الرسم العقاري")
                .descriptionFr("Document officiel délivré par la Conservation Foncière attestant de la propriété immatriculée d'un bien immobilier et de ses droits réels.")
                .descriptionAr("وثيقة رسمية تسلمها المحافظة العقارية تثبت الملكية المحفظة لعقار وحقوقه العينية.")
                .category(DocumentCategoryEnum.PROPERTY)
                .feeMad(null)
                .feeVariable(true)
                .feeNoteFr("Les frais varient selon la valeur du bien : droits d'immatriculation, honoraires du géomètre et taxes proportionnelles.")
                .feeNoteAr("تختلف الرسوم حسب قيمة العقار: رسوم التحفيظ، أتعاب المهندس المساح، والضرائب النسبية.")
                .processingDays(30)
                .locationFr("Conservation Foncière (ANCFCC)")
                .locationAr("المحافظة العقارية (الوكالة الوطنية للمحافظة العقارية والمسح العقاري والخرائطية)")
                .build());

        saveRequirements(doc,
                text("Justificatif d'acquisition du bien (acte de vente, héritage, etc.)", "ما يثبت اكتساب العقار (عقد بيع، إرث، إلخ)"),
                text("Levé topographique / bornage réalisé par un géomètre agréé", "التصميم الطوبوغرافي أو التحديد المنجز من طرف مهندس مساح معتمد"),
                text("Copie de la Carte Nationale d'Identité Électronique du/des propriétaire(s)", "نسخة من البطاقة الوطنية للتعريف الخاصة بالمالك (المالكين)"),
                text("Certificat de propriété ou réquisition d'immatriculation", "شهادة الملكية أو مطلب التحفيظ")
        );

        saveProcedures(doc,
                text("Déposer une réquisition d'immatriculation à la Conservation Foncière", "إيداع مطلب التحفيظ لدى المحافظة العقارية"),
                text("Faire réaliser le bornage du terrain par un géomètre agréé", "إنجاز عملية التحديد من طرف مهندس مساح معتمد"),
                text("Publication légale et délai d'opposition des tiers", "النشر القانوني وأجل تعرض الغير"),
                text("Paiement des droits et taxes proportionnels à la valeur du bien", "أداء الرسوم والضرائب النسبية لقيمة العقار"),
                text("Délivrance du titre foncier définitif", "تسليم الرسم العقاري النهائي")
        );

        saveLocation(doc,
                "Conservation Foncière — ANCFCC", "المحافظة العقارية - الوكالة الوطنية للمحافظة العقارية",
                "Conservation Foncière de la circonscription où se situe le bien", "المحافظة العقارية للدائرة التي يوجد بها العقار",
                PLACEHOLDER_PHONE, "contact@ancfcc.gov.ma", STANDARD_HOURS);
    }

    private void seedProofOfResidence() {
        Document doc = documentRepository.save(Document.builder()
                .code("proof-of-residence")
                .nameFr("Attestation de Résidence")
                .nameAr("شهادة السكنى")
                .descriptionFr("Document administratif simple attestant du lieu de résidence habituel du demandeur, requis pour de nombreuses démarches courantes.")
                .descriptionAr("وثيقة إدارية بسيطة تثبت مكان الإقامة الاعتيادي للطالب، تلزم لعدة إجراءات شائعة.")
                .category(DocumentCategoryEnum.RESIDENCE)
                .feeMad(new BigDecimal("10.00"))
                .processingDays(1)
                .locationFr("Commune ou Arrondissement du lieu de résidence")
                .locationAr("الجماعة أو المقاطعة بمكان الإقامة")
                .build());

        saveRequirements(doc,
                text("Carte Nationale d'Identité Électronique", "البطاقة الوطنية للتعريف الإلكترونية"),
                text("Justificatif d'adresse (facture d'eau, d'électricité ou contrat de bail)", "ما يثبت العنوان (فاتورة الماء أو الكهرباء أو عقد الكراء)")
        );

        saveProcedures(doc,
                text("Se présenter au bureau administratif de la commune ou de l'arrondissement", "التوجه إلى المكتب الإداري بالجماعة أو المقاطعة"),
                text("Présenter la pièce d'identité et le justificatif d'adresse", "تقديم بطاقة التعريف وما يثبت العنوان"),
                text("Payer les frais de 10 MAD", "أداء رسوم قدرها 10 دراهم"),
                text("Récupérer l'attestation délivrée sur place", "استلام الشهادة المسلمة في عين المكان")
        );

        saveLocation(doc,
                "Bureau Administratif — Commune / Arrondissement", "المكتب الإداري - الجماعة / المقاطعة",
                "Siège de la Commune ou de l'Arrondissement de votre lieu de résidence", "مقر الجماعة أو المقاطعة بمكان إقامتكم",
                PLACEHOLDER_PHONE, "contact@commune.gov.ma", STANDARD_HOURS);
    }

    private String[] text(String fr, String ar) {
        return new String[] { fr, ar };
    }

    @SafeVarargs
    private void saveRequirements(Document document, String[]... pairs) {
        int index = 1;
        for (String[] pair : pairs) {
            requirementRepository.save(DocumentRequirement.builder()
                    .documentId(document.getId())
                    .requirementFr(pair[0])
                    .requirementAr(pair[1])
                    .orderIndex(index++)
                    .build());
        }
    }

    @SafeVarargs
    private void saveProcedures(Document document, String[]... pairs) {
        int index = 1;
        for (String[] pair : pairs) {
            procedureRepository.save(DocumentProcedure.builder()
                    .documentId(document.getId())
                    .stepFr(pair[0])
                    .stepAr(pair[1])
                    .orderIndex(index++)
                    .build());
        }
    }

    private void saveLocation(Document document, String nameFr, String nameAr, String addressFr, String addressAr,
                               String phone, String email, String workingHours) {
        locationRepository.save(DocumentLocation.builder()
                .documentId(document.getId())
                .locationNameFr(nameFr)
                .locationNameAr(nameAr)
                .addressFr(addressFr)
                .addressAr(addressAr)
                .phone(phone)
                .email(email)
                .workingHours(workingHours)
                .build());
    }
}
