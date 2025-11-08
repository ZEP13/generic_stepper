Tu travailles sur une application de traitement de données métiers (par ex. import d’employés, commandes, performances médicales, etc.).
Chaque type d’objet doit passer par plusieurs étapes successives de validation et de calcul.

Exemples d’étapes :

    nettoyage de chaînes de caractères,

    suppression des doublons,

    calcul d’un score ou d’un montant total,

    filtrage selon un critère métier,

    envoi final en base ou vers une API externe.

Le but est de construire un moteur réutilisable et générique, qui exécute des Step<T> dans un ordre défini.
. Interface de base

Créer une interface générique :

 

 
public interface Step<T> {
List<T> execute(List<T> input);
}

 

Chaque étape reçoit une liste d’objets et retourne une nouvelle liste après traitement.
 getClass().getSimpleName(),
 
Étapes concrètes (exemples)

Chaque type d’objet a ses propres étapes, mais la structure du pipeline reste la même. Voila des exemples pour employés et produits mais cela pourrait être décliné a toute sorte d’autres données.

Exemples pour des employés :

    RemoveInactiveEmployeesStep implements Step<Employe>

    NormalizeNamesStep implements Step<Employe>

    ComputeSeniorityStep implements Step<Employe>

    PersistToDatabaseStep implements Step<Employe>

Exemples pour des produits :

    RemoveOutOfStockStep implements Step<Produit>

    ApplyDiscountStep implements Step<Produit>

    UpdateCatalogStep implements Step<Produit>

 
Base de données

Chaque pipeline peut se terminer par une étape PersistToDatabaseStep<T> :

    celle-ci utilise un Repository<T> générique avec les méthodes saveAll(List<T>).

    l’algo doit gérer les erreurs de persistance (rollback partiel, logs d’erreurs, etc.).

Règles métier et logique algorithmique

Chaque Step<T> peut contenir une logique complexe :

    calculs sur des champs,

    filtrage avec plusieurs conditions,

    enrichissement par des données externes (via un cache ou un repository),

    regroupement ou fusion d’éléments similaires.

Exemple :
Dans ComputeSeniorityStep, calculer l’ancienneté de chaque employé selon sa date d’embauche.
Dans ApplyDiscountStep, appliquer une réduction progressive selon la quantité en stock.

 
Exécution d’un pipeline

Le moteur doit pouvoir s’utiliser ainsi :
ProcessingPipeline<Employe> pipeline = new ProcessingPipeline<>();
pipeline.addStep(new RemoveInactiveEmployeesStep());
pipeline.addStep(new ComputeSeniorityStep());
pipeline.addStep(new PersistToDatabaseStep<>(new EmployeRepository()));
List<Employe> result = pipeline.execute(employes);
 
Contraintes

    Le moteur ne doit jamais connaître le type de données manipulé.

    Il doit être possible d’enchaîner des pipelines de types différents sans réécrire le moteur.

    Le code doit être robuste aux erreurs : une étape qui échoue ne bloque pas tout le traitement (sauf configuration contraire).

    Aucune logique conditionnelle sur les types (instanceof, if (T instanceof ...) interdit).

Bonus

    Système de rapport générique : chaque Step<T> peut renvoyer un objet StepReport contenant le nombre d’éléments traités, filtrés, ou en erreur.

    Pipeline imbriqué : un Step peut lui-même contenir un sous-pipeline.

    Configuration dynamique : chargement des étapes à exécuter depuis un fichier JSON (ordre et paramètres).

Super Bonus : Système de stockage dynamique
Objectif

Étendre le pipeline générique pour que les données traitées puissent être persistées sans table créée préalablement 

Le système doit permettre de stocker des objets de n’importe quel type T, sans que celui-ci soit mappé sur une table SQL spécifique.
