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
        return (NO_RIGHT);
    }

    public function getDefaultPage() {
        return (null);
    }

    public function parseAction(Page $page, User $user) {
        $page->setTemplate('connection');
        $action = $page->action;
        if ($user->isAdmin()) {
            Page::redirect(Url::toHome());
        } else if ($action === 'exit') {
            User::disconnect();
            Page::redirect(Url::toConnection());
        } else {
            if ($this->doConnection($page)) {
                Page::redirect(Url::toHome());
            }
        }
    }

    public function parseURI(URI $uri) {
        $uri->bind('action', 1);
    }

    private function doConnection(Page $page) {
        if (empty($_POST['connexion']) || $_POST['connexion'] !== 'Connexion'
                || empty($_POST['Upseudo']) || empty($_POST['Upasswd'])) {
            return (false);
        }

        $pseudo = $_POST['Upseudo'];
        $passwd = $_POST['Upasswd'];

        $page->pseudo = htmlentities($pseudo);
        return (User::connect($pseudo, $passwd) !== null);
    }

}