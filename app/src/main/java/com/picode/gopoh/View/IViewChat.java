package com.picode.gopoh.View;

public interface IViewChat {
    /**
     * Method ini di invoke ketika semua data chat yang ada atau yang berubah
     * selesai didapatkan
     */
    void onChatsObtained();

    /**
     * Method ini dipanggil ketika data room chat didapatkan
     */
    void onRoomChatObtained();
}
