package com.picode.gopoh.Interface;

import com.google.firebase.firestore.QuerySnapshot;

public interface OnGetChatComplete {
    void onChatsChanged(QuerySnapshot queryDocumentSnapshots);
}
