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
        if ($this->changeHomePage()) {
            Page::redirect(Url::toHomePage());
        }
    }

    public function parseURI(URI $uri) {
        
    }

    private function changeHomePage() {
        if (!empty($_POST['homedataset'])) {
            DBO::query('UPDATE `datasets` SET `home` = 0 WHERE `home` = 1;');
            DBO::query('UPDATE `datasets` SET `home` = 1 WHERE `id` = ' . intval($_POST['homedataset']));
            return (true);
        }
        return (false);
    }

}