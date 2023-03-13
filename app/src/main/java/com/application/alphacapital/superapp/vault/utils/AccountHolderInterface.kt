package com.alphaestatevault.utils

import com.alphaestatevault.model.AccountHolderListResponse

interface AccountHolderInterface
{
    fun okClick(getset : AccountHolderListResponse.Holder)
    fun onDismiss()
}