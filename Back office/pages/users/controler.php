<?php

/**
 * Copyright 2012-2013 The Apache Software Foundation
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at

 * http://www.apache.org/licenses/LICENSE-2.0

 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
**/

defined('EXEC') or die('Access forbidden');

class Controler extends AControler {

    public function getPerms() {
        return (READ_RIGHT);
    }

    public function getDefaultPage() {
        return (Url::toConnection());
    }

    public function parseAction(Page $page, User $user) {
        if ($page->action === 'passwd' && $user->can(WRITE_RIGHT)) {
            if ($this->changePasswd()) {
                Page::redirect(Url::toUsers());
            }
            $page->setView('passwd');
        } elseif ($page->action === 'del' && $user->can(DEL_RIGHT)) {
            if ($this->delUser($user)) {
                Page::redirect(Url::toUsers());
            }
            $page->setView('del');
        } elseif ($page->action === 'create' && $user->can(WRITE_RIGHT)) {
            if ($this->createUser()) {
                Page::redirect(Url::toUsers());
            }
            $page->setView('create');
        } else {
            $page->setView('list');
        }
    }

    public function parseURI(URI $uri) {
        $uri->bind('action', 1);
        $uri->bind('user', 2);
    }

    private function changePasswd() {
        if (!empty($_POST['Upasswd']) && !empty($_POST['Upasswd_confirm'])
                && !empty($_POST['UID']) && $_POST['Upasswd'] === $_POST['Upasswd_confirm']) {
            return (User::changePasswd($_POST));
        } else {
            return (false);
        }
    }

    private function createUser() {
        if (!empty($_POST['Upseudo']) && !empty($_POST['Upasswd'])
                && !empty($_POST['Upasswd_confirm']) && $_POST['Upasswd'] === $_POST['Upasswd_confirm']) {
            $_POST['Uaccess'] = ADMIN;
            $_POST['Uperms'] = NO_RIGHT;
            if (!empty($_POST['read_right'])) {
                $_POST['Uperms'] |= READ_RIGHT;
            }
            if (!empty($_POST['write_right'])) {
                $_POST['Uperms'] |= WRITE_RIGHT;
            }
            if (!empty($_POST['del_right'])) {
                $_POST['Uperms'] |= DEL_RIGHT;
            }
            return (User::create($_POST));
        } else {
            return (false);
        }
    }

    /**
     * Check if it is possible to remove an artist, and do it
     * 
     * @param User $user The user, for permission verification
     * @return boolean True if a group has been removed 
     */
    private function delUser(User $user) {
        if (empty($_POST['Upasswd']) || empty($_POST['UID'])) {
            return (false);
        }
        if (!$user->verifyPassword($_POST['Upasswd'])) {
            return (false);
        }
        return (User::delete($_POST));
    }

}