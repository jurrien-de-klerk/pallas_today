import 'package:flutter/material.dart';

import 'stories_near_me_screen.dart';
import 'post_story_screen.dart';

enum AppScreen { storiesNearMe, postStory }

class HomeShell extends StatefulWidget {
  const HomeShell({super.key});

  @override
  State<HomeShell> createState() => _HomeShellState();
}

class _HomeShellState extends State<HomeShell> {
  AppScreen _current = AppScreen.storiesNearMe;

  void _navigate(AppScreen screen) {
    setState(() => _current = screen);
    Navigator.of(context).pop();
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        backgroundColor: Theme.of(context).colorScheme.inversePrimary,
        title: Text(
          _current == AppScreen.storiesNearMe
              ? 'Stories near me'
              : 'Post story',
        ),
      ),
      drawer: Drawer(
        child: ListView(
          padding: EdgeInsets.zero,
          children: [
            DrawerHeader(
              decoration: BoxDecoration(
                color: Theme.of(context).colorScheme.inversePrimary,
              ),
              child: const Text('Pallas', style: TextStyle(fontSize: 24)),
            ),
            ListTile(
              leading: const Icon(Icons.near_me),
              title: const Text('Stories near me'),
              selected: _current == AppScreen.storiesNearMe,
              onTap: () => _navigate(AppScreen.storiesNearMe),
            ),
            ListTile(
              leading: const Icon(Icons.edit),
              title: const Text('Post story'),
              selected: _current == AppScreen.postStory,
              onTap: () => _navigate(AppScreen.postStory),
            ),
          ],
        ),
      ),
      body: _current == AppScreen.storiesNearMe
          ? const StoriesNearMeScreen()
          : const PostStoryScreen(),
    );
  }
}
