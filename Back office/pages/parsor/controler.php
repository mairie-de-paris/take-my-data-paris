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

        if ($action === 'edit' && $user->can(WRITE_RIGHT)) {
            if ($this->editParsor()) {
                Page::redirect(Url::toEditParsor($page->parsor));
            } else {
                $page->setView('edit');
            }
//            Exec::d($_POST);
//            Exec::e();
        } else if ($action === 'points' && $user->can(READ_RIGHT)) {
            $page->setView('points');
        } else {
            Page::redirect(Url::toDatasets());
        }
    }

    public function parseURI(URI $uri) {
        $uri->bind('action', 1);
        $uri->bind('parsor', 2);
    }

    private function editParsor() {
        return (false);
        if (!empty($_POST['name']) && !empty($_POST['type']) && !empty($_POST['fieldname']) &&
                !empty($_POST['fieldtype']) && !empty($_FILES['image'])) {
            return (Dataset::create($_POST, $_FILES['image']));
        } else {
            return (false);
        }
    }

}