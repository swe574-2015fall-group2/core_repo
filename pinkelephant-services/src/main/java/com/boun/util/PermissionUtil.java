package com.boun.util;

import com.boun.app.common.ErrorCode;
import com.boun.app.exception.PinkElephantRuntimeException;
import com.boun.data.Permission;
import com.boun.data.mongo.model.Role;
import com.boun.data.mongo.model.User;
import com.boun.data.mongo.model.UserRole;
import com.boun.data.session.PinkElephantSession;
import com.boun.http.request.BaseRequest;

public class PermissionUtil {
    public static void checkPermission(BaseRequest request, String groupId,  Permission permission) {

        User user = PinkElephantSession.getInstance().getUser(request.getAuthToken());
        UserRole role = user.getGroupRoles(groupId);
        boolean hasPermission = false;
        try {
            //TODO role.getGroupRoles() should not be null
            for (Role userRole : role.getGroupRoles()) {
                if (userRole.getPermissions().contains(permission)) {
                    hasPermission = true;
                }
            }
        }
        catch(Exception e){}

        if(!hasPermission) {
            throw new PinkElephantRuntimeException(400, ErrorCode.PERMISSION_NOT_FOUND, permission.toString());
        }
    }
}
