package es.uam.eps.tfg.app.tfgapp.controller;

public interface Callbacks {
    /**
     * Sets the title in the activity
     *
     * @param title id of the string resource that links the title
     */
    void setTitle(int title);

    /**
     * Replaces the current fragment for another one
     *
     * @param fragmentId id of the fragment which to switch
     */
    void navigateToFragment(int fragmentId);

}
