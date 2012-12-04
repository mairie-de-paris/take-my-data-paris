<?php

/**
 * Copyright 2012-2013 Mairie de Paris
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
        if ($page->action === 'del' && $user->can(DEL_RIGHT)) {
            if ($this->delChallenge($user)) {
                Page::redirect(Url::toUsers());
            }
            $page->setView('del');
        } elseif ($page->action === 'create' && $user->can(WRITE_RIGHT)) {
            if ($this->createChallenge()) {
                Page::redirect(Url::toUsers());
            }
            $page->setView('create');
        } else {
            $page->setView('list');
        }
    }

    public function parseURI(URI $uri) {
        $uri->bind('action', 1);
        $uri->bind('challenge', 2);
    }

    private function createChallenge() {
        if (!empty($_POST['dataset']) && !empty($_POST['number'])) {
            return (Challenge::create($_POST));
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
    private function delChallenge(User $user) {
        if (empty($_POST['Upasswd']) || empty($_POST['CID'])) {
            return (false);
        }
        if (!$user->verifyPassword($_POST['Upasswd'])) {
            return (false);
        }
        return (Challenge::delete($_POST));
    }

}