package id1212.werlinder.marcus.homework3.client.view;

public enum Commands {
    /**
     * Login
     */
    LOGIN,
    /**
     * Register a new user
     */
    REGISTER,
    /**
     * Upload a file to the server
     */
    UPLOAD,
    /**
     * Download a file from the server
     */
    DOWNLOAD,
    /**
     *Quit everything
     */
    QUIT,
    /**
     * If we want to list all the files that are available on the server
     */
    LIST,
    /**
     * We no longer want to be a member on this server
     */
    UNREGISTER,
    /**
     * If the user wants to be recognized if someone else reads/writes to a file
     */
    NOTIFY,
    /**
     * If we want to erase a file from the server
     */
    DELETE,
    /**
     * Bad command
     */
    BAD_COMMAND
}
