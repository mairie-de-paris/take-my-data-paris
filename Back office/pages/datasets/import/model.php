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

class Model implements IModel {

    public function process(Page $page) {
        $page->title = 'Administration';
        if (empty($_POST['source'])) {
            Page::redirect(Url::toEditParsor($page->parsor));
        }
        $page->source = urldecode($_POST['source']);
        
        if (($page->current = Parsor::getFromID($page->parsor)) === null) {
            Page::redirect(Url::toDatasets());
        } else {
            $page->current->setSource($page->source);
        }
    }

}