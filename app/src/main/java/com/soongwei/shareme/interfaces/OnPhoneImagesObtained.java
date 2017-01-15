package com.soongwei.shareme.interfaces;

import com.soongwei.shareme.objects.PhoneAlbum;

import java.util.Vector;

public interface OnPhoneImagesObtained {

    void onComplete( Vector<PhoneAlbum> albums );
    void onError();

}