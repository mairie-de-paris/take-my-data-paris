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
        $action = $page->action;

        if ($action === 'create' && $user->can(WRITE_RIGHT)) {
            if ($this->createDataset()) {
                Page::redirect(Url::toDatasets());
            } else {
                $page->setView('create');
            }
        } else if ($action === 'import' && $user->can(WRITE_RIGHT)) {
            if ($this->importDataset()) {
                Page::redirect(Url::toPoints($page->parsor));
            } else {
                $page->setView('import');
            }
        } else {
            $page->setView('list');
        }
    }

    public function parseURI(URI $uri) {
        $uri->bind('action', 1);
        $uri->bind('parsor', 2);
    }

    private function createDataset() {
        if (!empty($_POST['name']) && !empty($_POST['type']) && !empty($_POST['fieldname']) &&
                !empty($_POST['fieldtype']) && !empty($_POST['fielddesc']) && !empty($_POST['desc']) &&
                !empty($_FILES['image']) && !empty($_FILES['picto'])) {
            return (Dataset::create($_POST, $_FILES['image'], $_FILES['picto']));
        } else {
            return (false);
        }
    }

    private function importDataset() {
        if (!empty($_POST['lat']) && !empty($_POST['lon']) &&
                !empty($_POST['source']) && !empty($_POST['id'])) {
            return (Parsor::import($_POST));
        } else {
            return (false);
        }
    }

}