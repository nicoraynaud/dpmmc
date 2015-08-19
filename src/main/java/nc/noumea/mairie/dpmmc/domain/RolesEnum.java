package nc.noumea.mairie.dpmmc.domain;

public enum RolesEnum {

    ROLE_USER("ROLE_USER"),
    ROLE_ADMINISTRATION("ROLE_ADMINISTRATION"),
    ROLE_CREATION_FICHE("ROLE_CREATION_FICHE"),
    ROLE_MODIF_FICHE_N_BRIGADES("ROLE_MODIF_FICHE_N_BRIGADES"),
    ROLE_MODIF_FICHE_1_BRIGADE("ROLE_MODIF_FICHE_1_BRIGADE"),
    ROLE_CLOTURE_FICHE("ROLE_CLOTURE_FICHE"),
    ROLE_DECLOTURE_FICHE("ROLE_DECLOTURE_FICHE"),
    ROLE_RECH_FICHE("ROLE_RECH_FICHE"),
    ROLE_SUPPR_FICHE("ROLE_SUPPR_FICHE"),
    ROLE_STATS("ROLE_STATS");

    private final String text;

    /**
     * @param text
     */
    private RolesEnum(final String text) {
        this.text = text;
    }

    /* (non-Javadoc)
     * @see java.lang.Enum#toString()
     */
    @Override
    public String toString() {
        return text;
    }

    public static String valueOf(RolesEnum e) {
        return e.toString();
    }
}
