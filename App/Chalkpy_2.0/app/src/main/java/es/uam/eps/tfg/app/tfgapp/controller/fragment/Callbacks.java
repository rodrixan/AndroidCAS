package es.uam.eps.tfg.app.tfgapp.controller.fragment;

/**
 * Actions of the activity to be called from the fragments
 */
public interface Callbacks {
    /**
     * Sets the title in the activity
     *
     * @param title id of the string resource that links the title
     */
    void setTitle(int title);

    /**
     * Sets the subtitle in the activity
     *
     * @param subtitle id of the string resource that links the subtitle
     */
    void setSubtitle(int subtitle);

    /**
     * Sets the subtitle in the activity
     *
     * @param subtitle string for the subtitle
     */
    void setSubtitle(String subtitle);

    /**
     * Replaces the current fragment for another one
     *
     * @param fragmentId id of the fragment which to switch
     */
    void navigateToFragment(int fragmentId);

}
