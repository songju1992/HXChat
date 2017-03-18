package com.app.song.hxchat.utils;

import com.app.song.hxchat.BaseFragment;
import com.app.song.hxchat.fragment.ContactFragment;
import com.app.song.hxchat.fragment.ConversationFragment;
import com.app.song.hxchat.fragment.PluginFragment;

/**
 * Created by song on 2017/3/17 11:42
 */


public class FragmentFactory extends BaseFragment{

    private static ConversationFragment sConversationFragment;
    private static ContactFragment sContactFragment;
    private static PluginFragment sPluginFragment;

    public static BaseFragment getFragment(int position){
        BaseFragment baseFragment = null;
        switch (position) {
            case 0:
                if (sConversationFragment==null){
                    sConversationFragment = new ConversationFragment();
                }
                baseFragment = sConversationFragment;
                break;
            case 1:
                if (sContactFragment==null){
                    sContactFragment = new ContactFragment();
                }
                baseFragment = sContactFragment;
                break;
            case 2:
                if (sPluginFragment==null){
                    sPluginFragment = new PluginFragment();
                }
                baseFragment = sPluginFragment;
                break;
        }
        return baseFragment;

    }

}
