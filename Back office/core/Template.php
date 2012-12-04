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

final class Template {

    /**
     * @var Page The page to show 
     */
    private $page = null;

    /**
     * @var string The path for the modules
     */
    private $path = null;

    /**
     * Bind a page to the template and load the page
     * 
     * @param Page $page The page to bind
     * @param string $path The path to load the modules
     */
    private function __construct(Page $page, $path) {
        $this->page = $page;
        $this->path = $path;
        $this->page->load();
        $this->page->process();
    }

    /**
     * Load the default template for the specified page
     * 
     * @param Page $page The page
     * @param string $path The path to load the modules
     * @return Template The template
     */
    public static function load(Page $page, $path = '') {
        return (new Template($page, $path));
    }

    /**
     * Start the rendering of the template 
     * 
     * @param string $force If specify, ignore the page template and us this
     */
    public function render($force = null) {
        $name = $force;

        if ($force === null) {
            $name = $this->page->getTemplate();

            if (empty($name) || !file_exists(ROOT . 'templates/' . $name)) {
                $name = 'default';
            }
        }

        define('TEMPLATE_ROOT', '/templates/' . $name);
        define('RESSOURCES_ROOT', BASE_ROOT . TEMPLATE_ROOT);
        define('IMAGES_ROOT', RESSOURCES_ROOT . '/images');

        require_once(ROOT . $this->path . TEMPLATE_ROOT . '/default.php');
    }

    /**
     * Render the content of the page
     */
    protected function showPage() {
        $this->page->render();
    }

    /**
     * Get the title of the current page
     * 
     * @return string The title of the page 
     */
    protected function getTitle() {
        return ($this->page->title);
    }

    /**
     * Render the menu of the page
     */
    protected function showMenu() {
        include(ROOT . $this->path . '/modules/menu.php');
    }

    /**
     * Retreive Javascript scripts 
     */
    protected function showJS() {
        foreach ($this->page->getJS() as $script) {

            echo file_get_contents($script);
        }
    }

}